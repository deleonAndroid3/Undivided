package com.training.android.undivided.GroupSender.Module.Edit_Message;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.design.widget.TextInputLayout;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.training.android.undivided.GroupSender.Module.Send_Message.SendMessageActivity;
import com.training.android.undivided.GroupSender.Objects.Message;
import com.training.android.undivided.GroupSender.Objects.SMS;
import com.training.android.undivided.GroupSender.Utils.CustomPopupWindow;
import com.training.android.undivided.GroupSender.Utils.Toasts;
import com.training.android.undivided.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Hillary Briones on 1/28/2018.
 */

public class EditMessagePresenter implements EditMessageContract.Presenter {
    private static final String KEY_ORGANIZATION = "com.training.android.undivided.GroupSender.Module.Edit_Message.organization";
    private static final String KEY_SIGNATURE = "com.training.android.undivided.GroupSender.Module.Edit_Message.signature";
    private static final String DIVIDER = ",";

    private EditMessageContract.View mView;
    private Context mContext;
    private SharedPreferences mPref;
    private SharedPreferences.Editor mEditor;

    public EditMessagePresenter(Context context, EditMessageContract.View view) {
        mView = view;
        mView.setPresenter(this);
        mContext = context;
        mPref = PreferenceManager.getDefaultSharedPreferences(mContext);
    }




    @Override
    public void attach() {

    }

    @Override
    public void detach() {

    }

    @Override
    public Intent sendMessage(List<String> contracts) {
        String sms = new SMS.Builder()
                .setOrganization(mView.getOrganization())
                .setSignature(mView.getSignature())
                .setMessage(mView.getSMS())
                .create()
                .toString();

        Message message = new Message.Builder()
                .setTitle(getTitle(mContext, mView.getOrganization(), mView.getSignature()))
                .setSms(mView.getSMS())
                .setPhoneNumbers(getPhoneNumbersString(contracts))
                .build();

        return SendMessageActivity.newIntent(
                mContext, message, sms, (ArrayList<String>) contracts);
    }

    @Override
    public void saveOrganizationAndSignature(List<String> organizations, List<String> signatures) {
        mEditor = mPref.edit();
        mEditor.clear();
        String orgs = organizations.toString()
                .replace("[", "")
                .replace("]", "")
                .replaceAll(", ", DIVIDER);
        mEditor.putString(KEY_ORGANIZATION, orgs);

        String sigs = signatures.toString()
                .replace("[", "")
                .replace("]", "")
                .replaceAll(", ", DIVIDER);
        mEditor.putString(KEY_SIGNATURE, sigs);
        mEditor.apply();
    }

    @Override
    public List<String> getOrganizationList() {

        String organizations = mPref.getString(KEY_ORGANIZATION, "");
        List<String> list = new ArrayList<>();
        if (!"".equals(organizations)) {
            String[] item = organizations.split(DIVIDER);
            for (String s : item) {
                list.add(s);
            }
        }
        if (list.isEmpty()) {
            list.add(0, mContext.getString(R.string.unselected));
        }
        return list;
    }

    @Override
    public List<String> getSignatureList() {
        String signatures = mPref.getString(KEY_SIGNATURE, "");
        List<String> list = new ArrayList<>();
        if (!"".equals(signatures)) {
            String[] item = signatures.split(DIVIDER);
            for (String s : item) {
                list.add(s);
            }
        }
        if (list.isEmpty()) {
            list.add(0, mContext.getString(R.string.unselected));
        }
        return list;
    }

    @Override
    public void showPopup(final Context context, final Button button, final List<String> dataList) {
        final CustomPopupWindow popupWindow = new CustomPopupWindow.Builder(context)
                .setContentView(R.layout.layout_select_popup)
                .setWidth(ViewGroup.LayoutParams.WRAP_CONTENT)
                .setHeight(ViewGroup.LayoutParams.WRAP_CONTENT)
                .setFocus(true)
                .setOutsideCancel(true)
                .setElevation(5)
                .setAnimStyle(R.style.PopupWindowAnimStyle)
                .build()
                .showAtLocation(R.layout.activity_edit_message, Gravity.CENTER, 0, 0);

        TextView title = (TextView) popupWindow.findView(R.id.popup_title_text_view);

        if (null != button.getTag()) {
            String tips = button.getTag().toString();
            title.setText(tips);
            ((TextInputLayout) popupWindow.findView(R.id.text_input_layout)).setHint(tips);
        }

        ListView listView = (ListView) popupWindow.findView(R.id.select_item_list_view);
        final ArrayAdapter<String> adapter = new ArrayAdapter<>(
                context, android.R.layout.simple_list_item_1, dataList);
        listView.setAdapter(adapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                button.setText(dataList.get(position));
                popupWindow.dismiss();
            }
        });
        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                if (position > 0) {
                    Toasts.showToast(context.getString(R.string.delete_of, dataList.get(position)));
                    dataList.remove(position);
                    adapter.notifyDataSetChanged();
                }
                return true;
            }
        });

        final EditText editText = (EditText) popupWindow.findView(R.id.popup_edit_text);
        popupWindow.setOnClickListener(R.id.ok_button, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String item = editText.getText().toString();
                if (!TextUtils.isEmpty(item)) {
                    dataList.add(item);
                    button.setText(item);
                    adapter.notifyDataSetChanged();
                    popupWindow.dismiss();
                } else {
                    Toasts.showShort(R.string.cannot_be_empty);
                }

            }
        });

    }
    private String getTitle(Context context, String organization, String signature) {
        boolean isOrgNonNull = true;
        boolean isSigNonNull = true;

        if (null == organization) {
            isOrgNonNull = false;
        }

        if (null == signature) {
            isSigNonNull = false;
        }

        if (!isOrgNonNull && !isSigNonNull)
            return context.getResources().getString(R.string.NULL);

        return (isOrgNonNull ? "[" + organization + "]" : "")
                + (isOrgNonNull && isSigNonNull ? "-" : "")
                + (isSigNonNull ? signature : "");
    }

    private String getPhoneNumbersString(List<String> contracts) {
        return contracts.toString()
                .replace("[", "")
                .replace("]", "")
                .replaceAll(", ", ",");
    }


}
