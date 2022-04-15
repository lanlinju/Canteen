package com.example.canteen.fragments

import android.Manifest
import android.content.pm.PackageManager
import android.content.res.Resources
import android.graphics.BitmapFactory
import android.graphics.Color
import android.net.Uri
import android.os.Bundle
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.ui.unit.Constraints
import androidx.core.content.ContextCompat
import androidx.core.view.WindowCompat
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.example.canteen.R
import com.example.canteen.activities.MainActivity
import com.example.canteen.application.App
import com.example.canteen.databinding.FragmentGoodsDetailsBinding
import com.example.canteen.databinding.LayoutGoodsBottomSheetBinding
import com.example.canteen.models.Cart
import com.example.canteen.models.Category
import com.example.canteen.models.Goods
import com.example.canteen.models.UiState
import com.example.canteen.utilities.*
import com.example.canteen.viewmodels.CartViewModel
import com.example.canteen.viewmodels.FileViewModel
import com.example.canteen.viewmodels.GoodsViewModel
import com.example.canteen.viewmodels.HomeViewModel
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import kotlinx.coroutines.launch
import java.io.FileNotFoundException
import java.util.*


class GoodsDetailsFragment : Fragment() {

    private lateinit var binding: FragmentGoodsDetailsBinding
    private lateinit var goodsViewModel: GoodsViewModel
    private lateinit var goodsDetails: Goods
    private lateinit var fileViewModel: FileViewModel
    private lateinit var cartViewModel: CartViewModel
    private var goodsBottomSheetDialog: BottomSheetDialog? = null
    private lateinit var layoutGoodsBottomSheetBinding: LayoutGoodsBottomSheetBinding
    private lateinit var categoryList: List<Category>
    private var categoryName: String = ""

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        (requireActivity() as MainActivity).binding.smoothBottomBar.notShow()
        fileViewModel = ViewModelProvider(this)[FileViewModel::class.java]
        goodsViewModel = ViewModelProvider(this)[GoodsViewModel::class.java]
        cartViewModel = ViewModelProvider(this)[CartViewModel::class.java]
        binding =
            DataBindingUtil.inflate(inflater, R.layout.fragment_goods_details, container, false)
        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        goodsDetails = arguments?.getParcelable<Goods>("KEY_GOODS")!!
        categoryName = arguments?.getString("KEY_CATEGORY")!!
        categoryList = App.INSTANCE.categoryList!!
        binding.goods = goodsDetails
        requireActivity().apply {
            WindowCompat.setDecorFitsSystemWindows(window, false)
            //设置专栏栏和导航栏的底色，透明
            window.statusBarColor = Color.TRANSPARENT
        }
        setListeners()
        setObservers()

    }

    private fun setObservers() {
        goodsViewModel.isDeleted.observe(viewLifecycleOwner) {
            if (it == true) {
                getString(R.string.success_delete).showToast()
                binding.progressBar.notShow()
                requireActivity().onBackPressed()
            }
        }//更新的图片url路径
        fileViewModel.uploadImageInfo.observe(viewLifecycleOwner) {
            goodsDetails.thumbnail = it
        }
        goodsViewModel.currentState.observe(viewLifecycleOwner) { uiState ->
            when (uiState) {
                is UiState.IDLE -> layoutGoodsBottomSheetBinding.progressBar.notShow()
                is UiState.LOADING -> layoutGoodsBottomSheetBinding.progressBar.show()
                is UiState.ERROR -> uiState.message.showToast()
            }
        }
    }

    private fun setListeners() {
        with(binding) {
            buttonDelete.setOnClickListener {
                if (requireContext().getPreferenceManager()
                        .getString(Constants.KEY_ROSE_NAME)!! == Constants.KEY_ROSE_SYSTEM
                ) {//删除goods
                    binding.progressBar.show()
                    goodsViewModel.deleteGoods(goodsDetails)
                } else {
                    getString(R.string.less_authority).showToast()
                }
            }
            imageBack.setOnClickListener {
                requireActivity().onBackPressed()
            }
            textReadMore.setOnClickListener {
                if (textReadMore.text.toString() == getString(R.string.read_more)) {
                    textDescription.maxLines = Int.MAX_VALUE
                    textDescription.ellipsize = null
                    textReadMore.setText(R.string.read_less)
                } else {
                    textDescription.maxLines = 4
                    textDescription.ellipsize = TextUtils.TruncateAt.END
                    textReadMore.setText(R.string.read_more)
                }
            }
            textEdit.setOnClickListener {
                if (ContextCompat.checkSelfPermission(
                        requireActivity(),
                        Manifest.permission.READ_EXTERNAL_STORAGE
                    ) == PackageManager.PERMISSION_GRANTED
                ) {
                    showBottomSheetDialog()
                } else { //请求权限
                    requestPermissionLauncher.launch(Manifest.permission.READ_EXTERNAL_STORAGE)
                }
            }//添加购物车
            buttonAddCarts.setOnClickListener {
                val userId = requireContext().getPreferenceManager().getString(Constants.KEY_USER_ID)!!
                val cart = Cart(userId = userId, goodsId = goodsDetails.id!!, num = 1)
                cartViewModel.insertCart(cart)
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
                layoutGoodsBottomSheetBinding.imageProfile.setImageBitmap(bitmap)
                layoutGoodsBottomSheetBinding.textAddImage.visibility = View.INVISIBLE
                //上传图片
                ParseUriPath.getRealPathFromUri(requireContext(), uri)
                    ?.let { it -> fileViewModel.uploadFile(it) }
            } catch (e: Exception) {
                e.message?.showToast()
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
            imageSelectCategory.setOnClickListener {
                val items = Array<String>(categoryList.size) {
                    categoryList[it].cname
                }//选择货物对话框
                requireContext().singleChoiceDialog(items = items) {
                    this@GoodsDetailsFragment.categoryName = categoryList[it].cname
                    categoryName = categoryList[it].cname
                    //更新的goods的categoryId
                    goodsDetails.categoryId = categoryList[it].cid.toString()
                }
            }
            textCancel.setOnClickListener { goodsBottomSheetDialog?.dismiss() }
            imageClose.setOnClickListener { goodsBottomSheetDialog?.dismiss() }
            buttonSave.setOnClickListener { updateGoods() }
            goods = goodsDetails
            imageProfile.setImageUrl(goodsDetails.thumbnail)
            categoryName = this@GoodsDetailsFragment.categoryName
            textAddImage.notShow()
            inputPlace.visibility = View.VISIBLE
            textTitle.text = getString(R.string.update_goods_details)
            buttonSave.text = getString(R.string.update)
        }
    }

    private fun updateGoods() {
        layoutGoodsBottomSheetBinding.buttonSave.isClickable = false
        with(layoutGoodsBottomSheetBinding) {
            with(goodsDetails) {
                name = inputName.text.toString().split1()
                content = inputContent.text.toString().split1()
                price = inputPrice.text.toString().split1()
                place = inputPlace.text.toString().split1()
                number = inputNumber.text.toString().split1()
            }
            goods.toString().showLog()
            lifecycleScope.launch {
                goodsViewModel.updateGoods(goodsDetails).collect {
                    if (it == "1") {
                        getString(R.string.success).showToast()
                        layoutGoodsBottomSheetBinding.buttonSave.isClickable = true
                    } else {
                        getString(R.string.failed).showToast()
                        layoutGoodsBottomSheetBinding.buttonSave.isClickable = true
                    }

                }
            }
        }


    }

    private fun String.split1() = this.split(": ".toRegex()).toTypedArray()[1]

//    private fun isValidGoodsDetails(): Boolean { //验证输入的合法性
//        return when {
//            layoutGoodsBottomSheetBinding.inputName.text.toString().split(":".toRegex()).toTypedArray()[1].length == 1 -> {
//                displayToast("输入商品名字")
//                false
//            }
//            layoutGoodsBottomSheetBinding.inputContent.text.toString().split(":".toRegex()).toTypedArray()[1].length == 1 -> {
//                displayToast("输入商品描述")
//                false
//            }
//            categoryName.isEmpty() -> {
//                displayToast("添加货物类型")
//                false
//            }
//            layoutGoodsBottomSheetBinding.inputPrice.text.toString().split(":".toRegex()).toTypedArray()[1].length == 1 -> {
//                displayToast("输入商品价格")
//                false
//            }
//            layoutGoodsBottomSheetBinding.inputNumber.text.toString().split(":".toRegex()).toTypedArray()[1].length == 1 -> {
//                displayToast("输入商品数量")
//                false
//            }
//            else -> true
//        }
//    }


    private fun displayToast(text: String) {
        text.showToast()
    }

    override fun onDestroy() {
        super.onDestroy()
        (requireActivity() as MainActivity).apply {
            WindowCompat.setDecorFitsSystemWindows(window, true)
            //设置专栏栏和导航栏的底色，透明
            window.statusBarColor = Color.parseColor("#77A7EF")
            binding.smoothBottomBar.show()
        }
    }
}