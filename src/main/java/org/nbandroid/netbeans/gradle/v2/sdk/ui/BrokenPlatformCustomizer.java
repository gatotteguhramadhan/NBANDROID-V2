/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.nbandroid.netbeans.gradle.v2.sdk.ui;

import org.nbandroid.netbeans.gradle.v2.sdk.AndroidSdk;
import org.openide.DialogDisplayer;
import org.openide.NotifyDescriptor;
import org.openide.loaders.XMLDataObject;

/**
 *
 * @author arsi
 */
public class BrokenPlatformCustomizer extends javax.swing.JPanel {

    private final AndroidSdk platform;
    private final XMLDataObject holder;
    private final SdkValidListener listener;
    /**
     * Creates new form BrokenPlatformCustomizer
     */
    public BrokenPlatformCustomizer(AndroidSdk platform, XMLDataObject holder, SdkValidListener listener) {
        initComponents();
        this.platform = platform;
        this.holder = holder;
        this.listener = listener;
    }

    public static interface SdkValidListener {

        public void sdkValid();
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {
        java.awt.GridBagConstraints gridBagConstraints;

        downloadSdk = new javax.swing.JButton();
        changeFolder = new javax.swing.JButton();
        jLabel1 = new javax.swing.JLabel();

        setLayout(new java.awt.GridBagLayout());

        org.openide.awt.Mnemonics.setLocalizedText(downloadSdk, org.openide.util.NbBundle.getMessage(BrokenPlatformCustomizer.class, "BrokenPlatformCustomizer.downloadSdk.text")); // NOI18N
        downloadSdk.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                downloadSdkActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 3;
        add(downloadSdk, gridBagConstraints);

        org.openide.awt.Mnemonics.setLocalizedText(changeFolder, org.openide.util.NbBundle.getMessage(BrokenPlatformCustomizer.class, "BrokenPlatformCustomizer.changeFolder.text")); // NOI18N
        changeFolder.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                changeFolderActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.insets = new java.awt.Insets(6, 6, 6, 6);
        add(changeFolder, gridBagConstraints);

        org.openide.awt.Mnemonics.setLocalizedText(jLabel1, org.openide.util.NbBundle.getMessage(BrokenPlatformCustomizer.class, "BrokenPlatformCustomizer.jLabel1.text")); // NOI18N
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 1;
        add(jLabel1, gridBagConstraints);
    }// </editor-fold>//GEN-END:initComponents

    private void changeFolderActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_changeFolderActionPerformed
        SDKVisualPanelSelect visualPanelSelect = new SDKVisualPanelSelect();
        visualPanelSelect.setSdkName(platform.getDisplayName());
        NotifyDescriptor nd = new NotifyDescriptor.Confirmation(visualPanelSelect, "Select New Android SDK location", NotifyDescriptor.OK_CANCEL_OPTION, NotifyDescriptor.QUESTION_MESSAGE);
        Object notify = DialogDisplayer.getDefault().notify(nd);
        if (NotifyDescriptor.OK_OPTION.equals(notify)) {
            String sdkPath = visualPanelSelect.getSdkPath();
            platform.setSdkRootFolder(sdkPath);
            listener.sdkValid();
        }
    }//GEN-LAST:event_changeFolderActionPerformed

    private void downloadSdkActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_downloadSdkActionPerformed
        PanelDownloadToBrokenSDK panelDownloadToBrokenSDK = new PanelDownloadToBrokenSDK(platform);
        NotifyDescriptor nd = new NotifyDescriptor.Confirmation(panelDownloadToBrokenSDK, "Install Android SDK to broken folder", NotifyDescriptor.OK_CANCEL_OPTION, NotifyDescriptor.QUESTION_MESSAGE);
        Object notify = DialogDisplayer.getDefault().notify(nd);
        if (NotifyDescriptor.OK_OPTION.equals(notify)) {
            platform.setSdkRootFolder(platform.getSdkPath());
            listener.sdkValid();
        }
    }//GEN-LAST:event_downloadSdkActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton changeFolder;
    private javax.swing.JButton downloadSdk;
    private javax.swing.JLabel jLabel1;
    // End of variables declaration//GEN-END:variables
}
