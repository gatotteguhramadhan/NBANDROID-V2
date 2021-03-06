/*
 * Copyright 2000-2010 JetBrains s.r.o.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.android.tools.nbandroid.layoutlib;

import com.android.SdkConstants;
import com.android.ide.common.rendering.api.LayoutLog;
import com.android.io.FileWrapper;
import com.android.sdklib.internal.project.ProjectProperties;
import com.android.utils.ILogger;
import java.io.File;
import java.io.IOException;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Loads a {@link LayoutLibrary}
 */
public class LayoutLibraryLoader {

    private LayoutLibraryLoader() {
    }

    public static LayoutLibrary load(File platformFolder, ClassLoader classLoader) throws RenderingException, IOException {
        try {
            LayoutLibrary library;
            final ILogger logger = new LogWrapper();
            // We instantiate the local Bridge implementation and pass it to the LayoutLibrary instance
            Class<?> bridgeClass = classLoader.loadClass("com.android.layoutlib.bridge.Bridge");
            if (bridgeClass != null) {
                library = LayoutLibrary.load((com.android.ide.common.rendering.api.Bridge) bridgeClass.newInstance());

                final File fontFolder = new File(platformFolder, "data" + File.separator + "fonts");
                final File buildProp = new File(platformFolder, SdkConstants.FN_BUILD_PROP);
                final File attrs = new File(platformFolder, "data" + File.separator + "res" + File.separator + "values" + File.separator + "attrs.xml");
                final Map<String, String> buildPropMap = ProjectProperties.parsePropertyFile(new FileWrapper(buildProp), logger);
                final LayoutLog layoutLog = new LayoutLogWrapper();
                final Map<String, Map<String, Integer>> enumMap = ConfigGenerator.getEnumMap(attrs);
                if (library.init(buildPropMap, new File(fontFolder.getPath()), enumMap, layoutLog)) {
                    return library;
                }
            }
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(LayoutLibraryLoader.class.getName()).log(Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            Logger.getLogger(LayoutLibraryLoader.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            Logger.getLogger(LayoutLibraryLoader.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }
}
