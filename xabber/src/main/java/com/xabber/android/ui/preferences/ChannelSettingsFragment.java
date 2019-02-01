package com.xabber.android.ui.preferences;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.preference.Preference;
import android.preference.PreferenceFragment;
import android.preference.RingtonePreference;
import android.support.annotation.RequiresApi;
import android.support.annotation.StringRes;

import com.xabber.android.R;

@RequiresApi(api = Build.VERSION_CODES.O)
public class ChannelSettingsFragment extends PreferenceFragment {

    private NotificationManager notificationManager;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.preference_notifications);
        notificationManager = (NotificationManager) getActivity().getSystemService(Context.NOTIFICATION_SERVICE);
    }

    @Override
    public void onResume() {
        super.onResume();

        loadSound(R.string.events_sound_key, NotificationChannelUtils.ChannelType.privateChat);
        loadSound(R.string.events_sound_muc_key, NotificationChannelUtils.ChannelType.groupChat);
    }

    private void loadSound(@StringRes int resid, final NotificationChannelUtils.ChannelType type) {
        NotificationChannel channel = NotificationChannelUtils.getChannel(notificationManager, type);
        RingtonePreference preference = (RingtonePreference) getPreferenceScreen().findPreference(getString(resid));

        preference.setSummary(getSoundTitle(channel));
        preference.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {
            @Override
            public boolean onPreferenceChange(Preference preference, Object newValue) {
                NotificationChannelUtils.updateChannel(notificationManager, type,
                        Uri.parse(newValue.toString()), null, null);
                return true;
            }
        });
    }

    private String getSoundTitle(NotificationChannel channel) {
        if (channel == null) return null;
        Uri uri = channel.getSound();
        Ringtone ringtone = RingtoneManager.getRingtone(getActivity(), uri);
        return ringtone.getTitle(getActivity());
    }
}



