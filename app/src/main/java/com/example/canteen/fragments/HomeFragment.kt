package com.example.canteen.fragments

import android.Manifest
import android.annotation.SuppressLint
import android.content.Intent
import android.content.pm.PackageManager
import android.content.res.Resources
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Bundle
import android.util.Base64
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import androidx.core.graphics.drawable.toBitmap
import androidx.core.graphics.drawable.toDrawable
import androidx.core.view.GravityCompat
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.example.canteen.R
import com.example.canteen.activities.MainActivity
import com.example.canteen.activities.SignInActivity
import com.example.canteen.adapters.HomeViewPagerAdapter
import com.example.canteen.application.App
import com.example.canteen.databinding.FragmentHomeBinding
import com.example.canteen.databinding.LayoutGoodsBottomSheetBinding
import com.example.canteen.models.Category
import com.example.canteen.models.Goods
import com.example.canteen.models.UiState
import com.example.canteen.utilities.*
import com.example.canteen.viewmodels.FileViewModel
import com.example.canteen.viewmodels.GoodsViewModel
import com.example.canteen.viewmodels.HomeViewModel
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.tabs.TabLayoutMediator
import com.makeramen.roundedimageview.RoundedImageView
import kotlinx.coroutines.launch


class HomeFragment : Fragment() {

    private lateinit var binding: FragmentHomeBinding
    private lateinit var preferenceManager: PreferenceManager
    private lateinit var homeViewModel: HomeViewModel
    private lateinit var goodsViewModel: GoodsViewModel
    private lateinit var fileViewModel: FileViewModel
    private var goodsBottomSheetDialog: BottomSheetDialog? = null
    private lateinit var layoutGoodsBottomSheetBinding: LayoutGoodsBottomSheetBinding
    private var imageUploadPath = ""
    private var categoryName: String = ""

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        homeViewModel = ViewModelProvider(this)[HomeViewModel::class.java]
        fileViewModel = ViewModelProvider(this)[FileViewModel::class.java]
        goodsViewModel = ViewModelProvider(this)[GoodsViewModel::class.java]
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_home, container, false)
        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        preferenceManager = PreferenceManager(requireContext())
        loadUserDetails()
        if (!homeViewModel.isLoaded) {
            loadCategoryData()
        } else {
            setupTabWithViewPager2(homeViewModel.categoryList)
        }
        setObservers()
        setListeners()
    }

    private fun setObservers() {
        fileViewModel.uploadImageInfo.observe(viewLifecycleOwner) {
            imageUploadPath = it
        }
        goodsViewModel.currentState.observe(viewLifecycleOwner) { uiState ->
            when (uiState) {
                is UiState.IDLE -> layoutGoodsBottomSheetBinding.progressBar.notShow()
                is UiState.LOADING -> layoutGoodsBottomSheetBinding.progressBar.show()
                is UiState.ERROR -> uiState.message.showToast()

            }
        }
    }

    //申请读文件的权限
    private val requestPermissionLauncher =
        registerForActivityResult(ActivityResultContracts.RequestPermission()) {
            if (it) {
                showBottomSheetDialog()
            } else {
                getString(R.string.permission_less)
            }
        }

    //获取图片之后更新imageview 上传图片
    private val openDocumentLauncher =
        registerForActivityResult(ActivityResultContracts.OpenMultipleDocuments()) { uriList ->
            try {
                val uri: Uri = uriList[0]
                val inputStream = requireContext().contentResolver.openInputStream(uri)
                val bitmap = BitmapFactory.decodeStream(inputStream)
                layoutGoodsBottomSheetBinding.imageProfile.setImageDrawable(
                    bitmap.toDrawable(
                        resources
                    )
                )
                layoutGoodsBottomSheetBinding.textAddImage.visibility = View.INVISIBLE
                //上传图片
                ParseUriPath.getRealPathFromUri(requireContext(), uri)
                    ?.let { it -> fileViewModel.uploadFile(it) }
            } catch (e: Exception) {
                e.message?.showToast()
            }

        }


    private fun setListeners() {
        requireActivity().findViewById<RoundedImageView>(R.id.imageProfile).setOnClickListener {
            (requireActivity() as MainActivity).binding.drawerLayout.openDrawer(GravityCompat.START)
        }
        binding.inputSearch.setOnClickListener {
            findNavController().navigate(R.id.searchFragment)
        }
        binding.imageSignOut.setOnClickListener {
            requireContext().showDialog(getString(R.string.text_message_logout)) {
                preferenceManager.clear()
                startActivity(Intent(requireContext(), SignInActivity::class.java))
                onDestroy()
            }
        }
        binding.fabNewGoods.setOnClickListener {
            if (ContextCompat.checkSelfPermission(
                    requireActivity(),
                    Manifest.permission.READ_EXTERNAL_STORAGE
                ) == PackageManager.PERMISSION_GRANTED
            ) {
                showBottomSheetDialog()
            } else { //请求权限
                requestPermissionLauncher.launch(Manifest.permission.READ_EXTERNAL_STORAGE)
            }
        }

    }

    private fun showBottomSheetDialog() {//显示添加物品页面
        if (goodsBottomSheetDialog == null) {
            goodsBottomSheetDialog = BottomSheetDialog(requireContext())
            layoutGoodsBottomSheetBinding = DataBindingUtil.inflate(
                layoutInflater,
                R.layout.layout_goods_bottom_sheet,
                requireActivity().findViewById(R.id.goodsContainer),
                false
            )
            goodsBottomSheetDialog!!.setContentView(layoutGoodsBottomSheetBinding.root)
            initBottomView()
            resetBottomView()//去除显示的null
        }
        // ---- Optional section start ---- //
        val frameLayout: FrameLayout? = goodsBottomSheetDialog?.findViewById(
            com.google.android.material.R.id.design_bottom_sheet
        )
        if (frameLayout != null) {
            val bottomSheetBehavior = BottomSheetBehavior.from<View>(frameLayout)
            bottomSheetBehavior.peekHeight = Resources.getSystem().displayMetrics.heightPixels
            bottomSheetBehavior.state = BottomSheetBehavior.STATE_EXPANDED
        }
        // ---- Optional section end ---- //
        goodsBottomSheetDialog?.show()
    }

    private fun initBottomView() {
        with(layoutGoodsBottomSheetBinding) {
            layoutImage.setOnClickListener {
                //选择照片
                openDocumentLauncher.launch(
                    arrayOf(
                        "image/*",
                        "text/plain",
                        "video/*",
                        "audio/*"
                    )
                )//选择照片
            }
            buttonSave.setOnClickListener { uploadGoods() }//上传goods
            imageClose.setOnClickListener {
                resetBottomView()
                goodsBottomSheetDialog?.dismiss()
            }
            textCancel.setOnClickListener {
//                layoutGoodsBottomSheetBinding.inputName.text.toString().split(":".toRegex()).toTypedArray()[1].length.toString().showLog()
                resetBottomView()
                goodsBottomSheetDialog?.dismiss()
            }
            imageSelectCategory.setOnClickListener {
                val items = Array<String>(homeViewModel.categoryList.size) {
                    homeViewModel.categoryList[it].cname
                }//选择货物对话框
                requireContext().singleChoiceDialog(items = items) {
                    homeViewModel.categoryPosition = it
                    this@HomeFragment.categoryName = homeViewModel.categoryList[it].cname
                    categoryName = this@HomeFragment.categoryName
                }
            }
        }
    }

    @SuppressLint("UseCompatLoadingForDrawables")
    private fun resetBottomView() {//刷新视图
        val goods = Goods(content = "", name = "")
        imageUploadPath = ""
        layoutGoodsBottomSheetBinding.imageProfile.setImageDrawable(requireContext().getDrawable(R.drawable.background_image_goods))
        layoutGoodsBottomSheetBinding.categoryName = ""
        layoutGoodsBottomSheetBinding.goods = goods
    }

    private fun uploadGoods() { //新增货物

        with(layoutGoodsBottomSheetBinding) {
            if (isValidGoodsDetails()) {
                layoutGoodsBottomSheetBinding.buttonSave.isClickable = false
                val goods = Goods(
                    name = inputName.text.toString().split1(),
                    thumbnail = imageUploadPath,
                    categoryId = homeViewModel.categoryList[homeViewModel.categoryPosition].cid.toString(),
                    content = inputContent.text.toString().split1(),
                    price = inputPrice.text.toString().split1(),
                    number = inputNumber.text.toString().split1()
                )
                goods.toString().showLog()
                lifecycleScope.launch {
                    goodsViewModel.insertGoods(goods).collect {
                        if (it == "1") {
                            resetBottomView()
                            getString(R.string.upload_success).showToast()
                            layoutGoodsBottomSheetBinding.buttonSave.isClickable = true
                        } else {
                            getString(R.string.upload_failed).showToast()
                            layoutGoodsBottomSheetBinding.buttonSave.isClickable = true
                        }

                    }
                }
            }

        }

    }

    private fun String.split1() = this.trim().split(": ".toRegex()).toTypedArray()[1]

    //加载货物类型
    private fun loadCategoryData() {
        homeViewModel.getAllCategory().observe(viewLifecycleOwner) { baseResponse ->
            homeViewModel.isLoaded = true
            if (baseResponse.code == 404 || baseResponse.code == -1) {
                requireActivity().showDialog(baseResponse.msg) {
                }
                return@observe
            }
            if (baseResponse.data != null) {
                homeViewModel.categoryList = baseResponse.data!!
                App.INSTANCE.categoryList = baseResponse.data!!
                setupTabWithViewPager2(homeViewModel.categoryList)
            }
        }
    }

    private fun setupTabWithViewPager2(listCategory: List<Category>) {
        val tabLayout = binding.tabLayout
        val viewPager = binding.viewPager2
        viewPager.adapter = HomeViewPagerAdapter(listCategory, this)
        TabLayoutMediator(tabLayout, viewPager) { tab, position ->
            tab.text = listCategory[position].cname
        }.attach()
    }

    private fun loadUserDetails() {
        preferenceManager.getString(Constants.KEY_IMAGE)?.let {
            val bytes = Base64.decode(it, Base64.DEFAULT)
            val bitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.size)
            binding.imageProfile.setImageBitmap(bitmap)
        }
    }

    private fun isValidGoodsDetails(): Boolean { //验证输入的合法性
        return when {
            imageUploadPath.isEmpty() -> {
                displayToast("添加货物图片")
                false
            }
            layoutGoodsBottomSheetBinding.inputName.text.toString().split(":".toRegex()).toTypedArray()[1].length == 1 -> {
                displayToast("输入商品名字")
                false
            }
            layoutGoodsBottomSheetBinding.inputContent.text.toString().split(":".toRegex()).toTypedArray()[1].length == 1 -> {
                displayToast("输入商品描述")
                false
            }
            categoryName.isEmpty() -> {
                displayToast("添加货物类型")
                false
            }
            layoutGoodsBottomSheetBinding.inputPrice.text.toString().split(":".toRegex()).toTypedArray()[1].length == 1 -> {
                displayToast("输入商品价格")
                false
            }
            layoutGoodsBottomSheetBinding.inputNumber.text.toString().split(":".toRegex()).toTypedArray()[1].length == 1 -> {
                displayToast("输入商品数量")
                false
            }
            else -> true
        }
    }

    private fun displayToast(text: String) {
        text.showToast()
    }

}