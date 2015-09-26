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

package br.com.samuelzvir.meuabc.db;

import com.raizlabs.android.dbflow.annotation.Database;

@Database(name = MeuABCDatabase.NAME,
        version = MeuABCDatabase.VERSION,
        foreignKeysSupported = MeuABCDatabase.FOREIGN_KEYS_SUPPORTED
)
public class MeuABCDatabase {

    public static final String NAME = "MeuABCDB";
    public static final int VERSION = 1;
    public static final boolean FOREIGN_KEYS_SUPPORTED = true;
}
