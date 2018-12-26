/*
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *  http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */
package org.nbandroid.netbeans.gradle.v2.template.files;

import android.studio.imports.templates.Parameter;
import android.studio.imports.templates.Template;
import android.studio.imports.templates.TemplateMetadata;
import static android.studio.imports.templates.TemplateMetadata.ATTR_IS_LAUNCHER;
import static android.studio.imports.templates.TemplateMetadata.ATTR_PACKAGE_NAME;
import static android.studio.imports.templates.TemplateMetadata.ATTR_PARENT_ACTIVITY_CLASS;
import com.google.common.base.Strings;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import java.awt.Component;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;
import org.nbandroid.netbeans.gradle.v2.project.template.*;
import static org.nbandroid.netbeans.gradle.v2.project.template.AndroidProjectTemplatePanelVisualBasicSettings.PROP_PROJECT_SDK;
import org.nbandroid.netbeans.gradle.v2.project.template.freemarker.CircularParameterDependencyException;
import org.nbandroid.netbeans.gradle.v2.project.template.freemarker.ParameterValueResolver;
import org.nbandroid.netbeans.gradle.v2.project.template.freemarker.StringEvaluator;
import org.nbandroid.netbeans.gradle.v2.project.template.parameters.BooleanField;
import org.nbandroid.netbeans.gradle.v2.project.template.parameters.EnumField;
import org.nbandroid.netbeans.gradle.v2.project.template.parameters.ParameterUpdateListener;
import org.nbandroid.netbeans.gradle.v2.project.template.parameters.ParameterValueProvider;
import org.nbandroid.netbeans.gradle.v2.project.template.parameters.StringField;
import org.nbandroid.netbeans.gradle.v2.sdk.AndroidSdk;
import org.openide.WizardDescriptor;
import org.openide.WizardValidationException;
import org.openide.util.Exceptions;

public class ConfigureFileVisualAndroidSettings extends JPanel implements ItemListener, ParameterUpdateListener {

    private ConfigureFileWizardAndroidSettings panel;
    private AndroidSdk androidSdk;
    private Template currentTemplate;
    private String packageName;
    private String domain;
    public static final String PROP_FILE_PARAMETERS = "PROP_FILE_PARAMETERS";
    private final FileWizardIterator.Type type;

    public ConfigureFileVisualAndroidSettings(ConfigureFileWizardAndroidSettings panel, FileWizardIterator.Type type) {
        initComponents();
        this.panel = panel;
        this.type = type;
        label.setText(type.getDisplayName() + " settings");
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        label = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        parametersPanel = new javax.swing.JPanel();

        label.setFont(new java.awt.Font("Arial", 1, 18)); // NOI18N
        org.openide.awt.Mnemonics.setLocalizedText(label, org.openide.util.NbBundle.getMessage(ConfigureFileVisualAndroidSettings.class, "ConfigureFileVisualAndroidSettings.label.text")); // NOI18N

        parametersPanel.setLayout(new java.awt.GridLayout(0, 1));
        jScrollPane1.setViewportView(parametersPanel);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(label)
                .addContainerGap(503, Short.MAX_VALUE))
            .addComponent(jScrollPane1)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(label)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 352, Short.MAX_VALUE))
        );
    }// </editor-fold>//GEN-END:initComponents

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JLabel label;
    private javax.swing.JPanel parametersPanel;
    // End of variables declaration//GEN-END:variables

    @Override
    public void addNotify() {
        super.addNotify();
        //same problem as in 31086, initial focus on Cancel button
    }

    boolean valid(WizardDescriptor wizardDescriptor) {
        return true;
    }

    void store(WizardDescriptor d) {
        Map<Parameter, Object> userValues = new HashMap<>();
        Component[] components = parametersPanel.getComponents();
        for (Component component : components) {
            if (component instanceof ParameterValueProvider) {
                userValues.put(((ParameterValueProvider) component).getParameter(), ((ParameterValueProvider) component).getValue());
            }
        }
        d.putProperty(PROP_FILE_PARAMETERS, userValues);
    }

    void read(WizardDescriptor settings) {
        parametersPanel.removeAll();
        androidSdk = (AndroidSdk) settings.getProperty(PROP_PROJECT_SDK);
        currentTemplate = type.getTemplate();
        Collection<Parameter> parameters = currentTemplate.getMetadata().getParameters();
        packageName = (String) settings.getProperty(AndroidProjectTemplatePanelVisualBasicSettings.PROP_PROJECT_PACKAGE);
        domain = (String) settings.getProperty(AndroidProjectTemplatePanelVisualBasicSettings.PROP_PROJECT_DOMAIN);
        evaluateParameters();
        for (Parameter parameter : parameters) {
            if (isParameterVisible(parameter)) {
                switch (parameter.type) {
                    case STRING:
                        parametersPanel.add(new StringField(parameter, this));
                        break;
                    case BOOLEAN:
                        parametersPanel.add(new BooleanField(parameter, this));
                        break;
                    case ENUM:
                        parametersPanel.add(new EnumField(parameter, this));
                        break;
                    case SEPARATOR:
                        break;
                    default:
                        throw new AssertionError(parameter.type.name());

                }
            }
        }
    }

    @Override
    public void itemStateChanged(ItemEvent e) {
        panel.fireChangeEvent();
    }

    void validate(WizardDescriptor d) throws WizardValidationException {
        // nothing to validate
    }

    /**
     * If we are creating a new module, there are some fields that we need to
     * hide.
     */
    private boolean isParameterVisible(Parameter parameter) {
        boolean status = (!ATTR_PACKAGE_NAME.equals(parameter.id)
                && !ATTR_IS_LAUNCHER.equals(parameter.id)
                && !ATTR_PARENT_ACTIVITY_CLASS.equals(parameter.id));
        if (parameter.visibility != null && ("isInstantApp!false".equals(parameter.visibility) || "!isNewProject".equals(parameter.visibility) || "false".equals(parameter.visibility))) {
            return false;
        }
        return status;
    }

    private final StringEvaluator myEvaluator = new StringEvaluator();

    private void evaluateParameters() {

        Collection<Parameter> parameters = currentTemplate.getMetadata().getParameters();
        Set<String> excludedParameters = Sets.newHashSet();
        Map<String, Object> additionalValues = Maps.newHashMap();
        additionalValues.put(TemplateMetadata.ATTR_PACKAGE_NAME, packageName);
        additionalValues.put(TemplateMetadata.ATTR_COMPANY_DOMAIN, domain);
        additionalValues.put("generateLayout", true);
        additionalValues.put("includeInstantAppUrl", false);

        Map<String, Object> allValues = Maps.newHashMap(additionalValues);

        for (Parameter parameter : parameters) {
            String enabledStr = Strings.nullToEmpty(parameter.enabled);
            if (!enabledStr.isEmpty()) {
                boolean enabled = myEvaluator.evaluateBooleanExpression(enabledStr, allValues, true);
                if (!enabled) {
                    excludedParameters.add(parameter.id);
                }
            }
            if (!isParameterVisible(parameter)) {
                excludedParameters.add(parameter.id);
                continue;
            }
            String visibilityStr = Strings.nullToEmpty(parameter.visibility);
            if (!visibilityStr.isEmpty()) {
                boolean visible = myEvaluator.evaluateBooleanExpression(visibilityStr, allValues, true);
                if (!visible) {
                    excludedParameters.add(parameter.id);
                }
            }
        }
    }

    @Override
    public void parameterUpdated(Parameter parameter, Object value) {
        Map<Parameter, Object> userValues = new HashMap<>();
        List<Parameter> parameters = new ArrayList<>();
        Map<String, Object> additionalValues = Maps.newHashMap();
        additionalValues.put(TemplateMetadata.ATTR_PACKAGE_NAME, packageName);
        additionalValues.put(TemplateMetadata.ATTR_COMPANY_DOMAIN, domain);
        additionalValues.put("includeInstantAppUrl", false);
        Component[] components = parametersPanel.getComponents();
        for (Component component : components) {
            if (component instanceof ParameterValueProvider) {
                parameters.add(((ParameterValueProvider) component).getParameter());
                boolean related = ((ParameterValueProvider) component).getParameter().isSugested(parameter);
                if (!related || parameter.equals(((ParameterValueProvider) component).getParameter())) {
                    userValues.put(((ParameterValueProvider) component).getParameter(), ((ParameterValueProvider) component).getValue());
                }
            }
        }
        final Map<Parameter, Object> resolved = userValues;
        try {
            Map<Parameter, Object> resolve = ParameterValueResolver.resolve(parameters, userValues, additionalValues);
            resolved.putAll(resolve);
        } catch (CircularParameterDependencyException ex) {
            Exceptions.printStackTrace(ex);
        }
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                for (Component component : components) {
                    if (component instanceof ParameterValueProvider) {
                        ((ParameterValueProvider) component).update(resolved);
                    }
                }
            }
        };
        SwingUtilities.invokeLater(runnable);
    }
}
