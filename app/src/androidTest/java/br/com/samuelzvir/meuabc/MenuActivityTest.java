/*
 * Copyright 2015 Samuel Yuri Zvir
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package br.com.samuelzvir.meuabc;

import android.content.Intent;
import android.test.ActivityUnitTestCase;
import android.test.suitebuilder.annotation.MediumTest;

import br.com.ema.activities.MenuActivity;

public class MenuActivityTest extends ActivityUnitTestCase<MenuActivity> {


    public MenuActivityTest(){
        super(MenuActivity.class);
    }

    Intent menuIntent;

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        menuIntent = new Intent(getInstrumentation()
                .getTargetContext(), MenuActivity.class);
        startActivity(menuIntent, null, null);
    }

    @MediumTest
    public void testHello(){
//        startActivity(menuIntent, null, null);
//        final Button addUserButton =
//                (Button) getActivity()
//                        .findViewById(R.id.button);
//        addUserButton.performClick();

    }

}