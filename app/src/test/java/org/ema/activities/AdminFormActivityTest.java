package org.ema.activities;

import android.content.Context;
import android.content.Intent;
import android.widget.EditText;

import org.ema.R;
import org.ema.activities.admin.AdminFormActivity;
import org.ema.activities.admin.MenuActivity;
import org.ema.entities.Admin;
import org.ema.entities.AppConfiguration;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.powermock.core.classloader.annotations.PowerMockIgnore;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.core.classloader.annotations.SuppressStaticInitializationFor;
import org.powermock.modules.junit4.PowerMockRunner;
import org.powermock.modules.junit4.PowerMockRunnerDelegate;
import org.powermock.modules.junit4.rule.PowerMockRule;
import org.robolectric.Robolectric;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.RuntimeEnvironment;
import org.robolectric.Shadows;

import java.util.Arrays;

import io.realm.Realm;
import io.realm.RealmConfiguration;
import io.realm.RealmQuery;
import io.realm.RealmResults;
import io.realm.internal.RealmCore;
import io.realm.log.RealmLog;

import static org.mockito.Matchers.any;
import static org.powermock.api.mockito.PowerMockito.doNothing;
import static org.powermock.api.mockito.PowerMockito.mock;
import static org.powermock.api.mockito.PowerMockito.mockStatic;
import static org.powermock.api.mockito.PowerMockito.when;
import static org.powermock.api.mockito.PowerMockito.whenNew;

import static  org.junit.Assert.*;


@RunWith(PowerMockRunner.class)
@PowerMockRunnerDelegate(RobolectricTestRunner.class)
@PowerMockIgnore({"org.mockito.*", "org.robolectric.*", "android.*"})
@SuppressStaticInitializationFor("io.realm.internal.Util")
@PrepareForTest({Realm.class, RealmConfiguration.class, RealmQuery.class, RealmResults.class, RealmCore.class, RealmLog.class})
public class AdminFormActivityTest {

    @Rule
    public PowerMockRule rule = new PowerMockRule();

    private Realm mockRealm;

    @Before
    public void setup() throws Exception {

        // Setup Realm to be mocked. The order of these matters
        mockStatic(RealmCore.class);
        mockStatic(RealmLog.class);
        mockStatic(Realm.class);
        mockStatic(RealmConfiguration.class);

        // Create the mock
        final Realm mockRealm = mock(Realm.class);
        final RealmConfiguration mockRealmConfig = mock(RealmConfiguration.class);

        Realm.init(RuntimeEnvironment.application);
        doNothing().when(Realm.class,"init",Mockito.any(Context.class));
        doNothing().when(RealmCore.class);
        RealmCore.loadLibrary(any(Context.class));

        whenNew(RealmConfiguration.class).withAnyArguments().thenReturn(mockRealmConfig);

        when(Realm.getDefaultInstance()).thenReturn(mockRealm);

        when(mockRealm.createObject(AppConfiguration.class)).thenReturn(new AppConfiguration());
        RealmQuery<Admin> adminRealmQuery = mock(RealmQuery.class);

        when(adminRealmQuery.findFirst()).thenReturn(new Admin());
        when(mockRealm.where(Admin.class)).thenReturn(adminRealmQuery);

        this.mockRealm = mockRealm;
    }

    @Test
    public void appFillFirstFormWithSuccess() {
        MockitoAnnotations.initMocks(this);
        AdminFormActivity adminFormActivity = Robolectric.buildActivity(AdminFormActivity.class).create().get();
        EditText name = adminFormActivity.findViewById(R.id.newName);
        name.setText("test-1");
        EditText email = adminFormActivity.findViewById(R.id.emailTxt);
        email.setText("test-1@email.com");
        EditText password = adminFormActivity.findViewById(R.id.passwordTxt);
        password.setText("123test-1234");
        assertTrue(adminFormActivity.findViewById(R.id.saveBtn).performClick());

        Intent actual = Shadows.shadowOf(adminFormActivity.getApplication()).getNextStartedActivity();
        Intent expectedIntent = new Intent(adminFormActivity, MenuActivity.class);
        Assert.assertEquals(expectedIntent.getComponent(), actual.getComponent());
    }
}
