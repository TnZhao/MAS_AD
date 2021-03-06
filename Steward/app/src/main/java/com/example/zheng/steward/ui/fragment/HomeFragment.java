package com.example.zheng.steward.ui.fragment;

import android.content.Intent;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import com.example.zheng.steward.MainActivity;
import com.example.zheng.steward.R;
import com.example.zheng.steward.app.AppConst;
import com.example.zheng.steward.ui.activity.OrderManagerActivity;
import com.example.zheng.steward.ui.base.BaseFragment;
import com.example.zheng.steward.ui.presenter.HomeFgPresenter;
import com.example.zheng.steward.ui.view.IHomeFgView;
import com.example.zheng.steward.utils.SPUtils;
import com.example.zheng.steward.utils.UIUtils;
import com.example.zheng.steward.widget.MyGridView;
import com.jwsd.libzxing.OnQRCodeScanCallback;
import com.jwsd.libzxing.QRCodeManager;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.Bind;

/**
 * Created by zheng on 2018/3/30.
 * 首页fragment
 */

public class HomeFragment extends BaseFragment<IHomeFgView, HomeFgPresenter> implements IHomeFgView {

    private static final String TAG = "HomeFragment";

    /**
     * 工具栏上的title
     */
    @Bind(R.id.tvToolbarTitle)
    public TextView mToolbarTitle;

    /**
     * 扫描二维码按钮
     */
    @Bind(R.id.ibScanMenu)
    public Button mScanBtn;

    /**
     * 切换当月数据和累计数据的按钮
     */
    @Bind(R.id.view_cumulative_btn)
    Button mCumulativeBtn;

    /**
     * 查看当月或累计金额标签
     */
    @Bind(R.id.current_or_total_apply)
    TextView currentOrTotalApplyText;

    /**
     * 查看当月或累计进件数标签
     */
    @Bind(R.id.current_or_total_into)
    TextView currentOrTotalIntoText;

    /**
     * 当月或累计金额
     */
    @Bind(R.id.current_or_total_money)
    TextView currentOrTotalMoneyText;

    /**
     * 当月或累计进件数
     */
    @Bind(R.id.current_or_total_order)
    TextView currentOrTotalOrderText;

    /**
     * 券码验证按钮
     */
    @Bind(R.id.code_verification_btn)
    Button mVerificationBtn;

    /**
     * 开始分期按钮
     */
    @Bind(R.id.stages_btn)
    Button mStagesBtn;

    /**
     * 订单入口GridView
     */
    @Bind(R.id.order_interface_gridview)
    MyGridView mInterfaceGridView;

    /**
     * 新手须知按钮
     */
    @Bind(R.id.new_guidelines)
    ImageButton newGuidelinesBtn;

    /**
     * 初始化gridView数据源
     */
    private String[] from = {"image", "title"};
    private int[] to = {R.id.order_interface_image, R.id.order_interface_title};

    /**
     * 查看累计按钮状态标志位
     */
    private Integer cumulativeBtnStatus = 0;

    @Override
    protected HomeFgPresenter createPresenter() {
        return new HomeFgPresenter((MainActivity) getActivity());
    }

    @Override
    protected int provideContentViewId() {
        return R.layout.tab_home;
    }

    @Override
    public void initView(View rootView) {
        super.initView(rootView);
    }

    @Override
    public void initData() {
        mPresenter.loadHomeData();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        //注册onActivityResult
        QRCodeManager.getInstance().with(getActivity()).onActivityResult(requestCode, resultCode, data);
    }

    /**
     * 初始化各按钮的点击监听事件
     */
    @Override
    public void initListener() {

        mScanBtn.setOnClickListener(view -> scanButtonClicked());

        mCumulativeBtn.setOnClickListener(view -> cumulativeButtonClicked());
        mVerificationBtn.setOnClickListener(view -> verificationBtnClicked());

        mStagesBtn.setOnClickListener(view -> startStageBtnClicked());
        newGuidelinesBtn.setOnClickListener(view -> newGuidelinesBtnClicked());

        SimpleAdapter adapter = new SimpleAdapter(getActivity(), getList(), R.layout.interface_gridview_item, from, to);
        mInterfaceGridView.setAdapter(adapter);
        mInterfaceGridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                switch (position) {
                    case 0:
                        Log.d(TAG, "onItemClick: 申请件查询");
                        getActivity().startActivity(new Intent(getContext(), OrderManagerActivity.class));
                        getActivity().overridePendingTransition(R.anim.right_in, R.anim.left_out);
                        break;
                    case 1:
                        Log.d(TAG, "onItemClick: 待放款件查询");
                        getActivity().startActivity(new Intent(getContext(), OrderManagerActivity.class));
                        getActivity().overridePendingTransition(R.anim.right_in, R.anim.left_out);
                        break;
                    case 2:
                        Log.d(TAG, "onItemClick: 放款件查询");
                        getActivity().startActivity(new Intent(getContext(), OrderManagerActivity.class));
                        getActivity().overridePendingTransition(R.anim.right_in, R.anim.left_out);
                        break;
                    case 3:
                        Log.d(TAG, "onItemClick: 逾期件查询");
                        getActivity().startActivity(new Intent(getContext(), OrderManagerActivity.class));
                        getActivity().overridePendingTransition(R.anim.right_in, R.anim.left_out);
                        break;
                    case 4:
                        Log.d(TAG, "onItemClick: 优惠券查询");
                        getActivity().startActivity(new Intent(getContext(), OrderManagerActivity.class));
                        getActivity().overridePendingTransition(R.anim.right_in, R.anim.left_out);
                        break;
                    case 5:
                        Log.d(TAG, "onItemClick: 客户画像");
                        getActivity().startActivity(new Intent(getContext(), OrderManagerActivity.class));
                        getActivity().overridePendingTransition(R.anim.right_in, R.anim.left_out);
                        break;
                    default:
                        Log.d(TAG, "onItemClick: 无效点击");
                        break;
                }
            }
        });
    }

    /**
     * 初始化gridView数据源
     *
     * @return
     */
    private List<Map<String, Object>> getList() {
        List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();

        String[] titles = new String[]{"申请件查询", "待放款件查询", "放款件查询", "逾期件查询", "优惠券查询", "客户画像"};
        Integer[] images = new Integer[]{R.mipmap.ic_apply_query, R.mipmap.ic_for_lending, R.mipmap.ic_lended, R.mipmap.ic_overdue, R.mipmap.ic_coupon, R.mipmap.ic_customer};

        for (int i = 0; i < images.length; i++) {
            Map<String, Object> map = new HashMap<>();
            map.put("title", titles[i]);
            map.put("image", images[i]);
            list.add(map);
        }
        return list;
    }

    private void scanButtonClicked() {
        Log.i(TAG, "scanButtonClicked: 扫描二维码按钮点击");
        QRCodeManager.getInstance().with(getActivity()).setReqeustType(1).scanningQRCode(new OnQRCodeScanCallback() {
            @Override
            public void onCompleted(String s) {
                Log.i(TAG, "onCompleted: ");
            }

            @Override
            public void onError(Throwable throwable) {
                Log.i(TAG, "onError: ");
            }

            @Override
            public void onCancel() {
                Log.i(TAG, "onCancel: ");
            }
        });
    }

    /**
     * 查看金额按钮点击事件
     */
    private void cumulativeButtonClicked() {
        if (cumulativeBtnStatus == 0) {
            currentMonthAction();
        } else if (cumulativeBtnStatus == 1) {
            cumulativeAction();
        }
    }

    /**
     * 查看当月动作
     */
    private void currentMonthAction() {
        cumulativeBtnStatus = 1;
        mCumulativeBtn.setBackgroundResource(R.mipmap.ic_view_current_month);
        currentOrTotalApplyText.setText("累计申请金额（万元）");
        currentOrTotalIntoText.setText("累计进件数 (笔）");

        currentOrTotalMoneyText.setText(SPUtils.getInstance(UIUtils.getContext()).getString(AppConst.Merchant.TOTAL_AMOUNT, "0"));
        currentOrTotalOrderText.setText(SPUtils.getInstance(UIUtils.getContext()).getString(AppConst.Merchant.TOTAL_APPLY, "0"));
    }

    /**
     * 查看累计动作
     */
    private void cumulativeAction() {
        cumulativeBtnStatus = 0;
        mCumulativeBtn.setBackgroundResource(R.mipmap.ic_view_cumulative);
        currentOrTotalApplyText.setText("当月申请金额（元）");
        currentOrTotalIntoText.setText("当月进件数 (笔）");

        currentOrTotalMoneyText.setText(SPUtils.getInstance(UIUtils.getContext()).getString(AppConst.Merchant.MONTH_AMOUNT, "0"));
        currentOrTotalOrderText.setText(SPUtils.getInstance(UIUtils.getContext()).getString(AppConst.Merchant.MONTH_APPLY, "0"));
    }

    /**
     * 券码验证按钮点击事件
     */
    private void verificationBtnClicked() {
        Log.d(TAG, "codeVerificationBtnClicked: 券码验证按钮点击");
        ManualVerificationFragment manualVerificationDialog = new ManualVerificationFragment();
        manualVerificationDialog.show(getFragmentManager(), TAG);
    }

    /**
     * 开始分期按钮点击事件
     */
    private void startStageBtnClicked() {
        Log.d(TAG, "startStageBtnClicked: 开始分期按钮点击");
        QRCodeFragment qrCodeDialog = new QRCodeFragment();
        qrCodeDialog.show(getFragmentManager(), TAG);
    }

    /**
     * 新手须知按钮点击事件
     */
    private void newGuidelinesBtnClicked() {
        Log.d(TAG, "newGuidelinesBtnClicked: 新手须知按钮点击");
    }

    @Override
    public TextView getSumTextView() {
        return currentOrTotalMoneyText;
    }

    @Override
    public TextView getOrderNumTextView() {
        return currentOrTotalOrderText;
    }

    @Override
    public TextView getToolBarTitleTextView() {
        return mToolbarTitle;
    }
}
