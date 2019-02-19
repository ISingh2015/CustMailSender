/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.cust.mailsender.controller;

import com.cust.custmailsender.MailSender;
import com.cust.domain.vo.ElegantMailList;
import com.cust.domain.vo.ElegantMarketMail;
import com.cust.persistance.managers.BuySellManager;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.ArrayList;
import javax.swing.JFileChooser;
import javax.swing.SwingUtilities;
import javax.swing.filechooser.FileNameExtensionFilter;

/**
 *
 * @author Zed
 */
public class EmailSenderController implements ActionListener {

    private MailSender mailSender;
    private String controlAction;
    private JFileChooser chooser = new JFileChooser();
    private ArrayList<ElegantMailList> mailList;

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getActionCommand().equalsIgnoreCase("send")) {
            Thread sendThread = new Thread(new Runnable() {
                @Override
                public void run() {
                    SwingUtilities.invokeLater(new Runnable() {
                        public void run() {
                            conntectAndSendEmailAction();
                            mailSender.errorLabel.setText("Emails Sent ... OK");
                            init();
                        }
                    });
                }
            });
            sendThread.start();
        } else if (e.getActionCommand().equalsIgnoreCase("tohelp")) {
            Thread toThread = new Thread(new Runnable() {
                @Override
                public void run() {
                    SwingUtilities.invokeLater(new Runnable() {
                        public void run() {
                            setEmailList();
                            mailSender.toCCHelp.searchLabel.setText("Select TO Address");
//                            mailSender.toCCHelp.emailList.setModel(defaultListmodel);
                            setControlAction("toHelp");
                            mailSender.toCCHelp.setVisible(true);
                        }
                    });
                }
            });
            toThread.start();
        } else if (e.getActionCommand().equalsIgnoreCase("cchelp")) {
            Thread ccThread = new Thread(new Runnable() {
                @Override
                public void run() {
                    SwingUtilities.invokeLater(new Runnable() {
                        public void run() {
                            setEmailList();
                            mailSender.toCCHelp.searchLabel.setText("Select CC Address");
//                            mailSender.toCCHelp.emailList.setModel(defaultListmodel);
                            setControlAction("ccHelp");
                            mailSender.toCCHelp.setVisible(true);
                        }
                    });
                }
            });
            ccThread.start();

        } else if (e.getActionCommand().equalsIgnoreCase("bcchelp")) {
            Thread bccThread = new Thread(new Runnable() {
                @Override
                public void run() {
                    SwingUtilities.invokeLater(new Runnable() {
                        public void run() {
                            setEmailList();
                            mailSender.toCCHelp.searchLabel.setText("Select BCC Address");
//                            mailSender.toCCHelp.emailList.setModel(defaultListmodel);
                            setControlAction("bccHelp");
                            mailSender.toCCHelp.setVisible(true);
                        }
                    });
                }
            });
            bccThread.start();

        } else if (e.getActionCommand().equalsIgnoreCase("toccok")) {
            mailSender.toCCHelp.setClickAction(1);
            mailSender.toCCHelp.setVisible(false);
            if (getControlAction().equalsIgnoreCase("tohelp")) {
                if (mailSender.toCCHelp.emailList.getSelectedValuesList().size() > 0) {
                    mailSender.toTextField.setText(mailSender.toCCHelp.emailList.getSelectedValuesList().toString());
                }
            } else if (getControlAction().equalsIgnoreCase("cchelp")) {
                if (mailSender.toCCHelp.emailList.getSelectedValuesList().size() > 0) {
                    mailSender.ccTextField.setText(mailSender.toCCHelp.emailList.getSelectedValuesList().toString());
                }
            } else if (getControlAction().equalsIgnoreCase("bcchelp")) {
                if (mailSender.toCCHelp.emailList.getSelectedValuesList().size() > 0) {
                    mailSender.bccTextField.setText(mailSender.toCCHelp.emailList.getSelectedValuesList().toString());
                }
            }
        } else if (e.getActionCommand().equalsIgnoreCase("tocccancel")) {
            mailSender.toCCHelp.setClickAction(0);
            mailSender.toCCHelp.setVisible(false);
        } else if (e.getActionCommand().equalsIgnoreCase("fileButton")) {
            try {
                FileNameExtensionFilter filter = new FileNameExtensionFilter("HTML Files", "htm", "html");
                chooser.setFileFilter(filter);
                int returnVal = chooser.showOpenDialog(mailSender);
                if (returnVal == JFileChooser.APPROVE_OPTION) {
                    File f = new File(chooser.getSelectedFile().getAbsolutePath());
                    mailSender.messageEditorPane.setPage(f.toURI().toURL());
                }
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        } else if (e.getActionCommand().equalsIgnoreCase("cancel")) {
            System.exit(0);
        }
    }

    private void init() {
        mailSender.toTextField.setText("");
        mailSender.ccTextField.setText("");
        mailSender.bccTextField.setText("");
        mailSender.subTextField.setText("");
        mailSender.messageEditorPane.setText("");
    }

    private void conntectAndSendEmailAction() {
        BuySellManager buySellManager = new BuySellManager();
        ArrayList<ElegantMarketMail> elegantMarketEmailList = createEmailContent();
        try {
            elegantMarketEmailList = buySellManager.saveGlobalMailList(elegantMarketEmailList);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private ArrayList<ElegantMarketMail> createEmailContent() {
//        System.out.println(mailSender.messageEditorPane.getText().length());
        ArrayList<ElegantMarketMail> emailList = new ArrayList<>();
        ElegantMarketMail elegantMarketMail = new ElegantMarketMail();
        elegantMarketMail.setEmailToAddress(mailSender.toTextField.getText());
        elegantMarketMail.setEmailCcAddress(mailSender.ccTextField.getText());
        elegantMarketMail.setEmailBccAddress(mailSender.bccTextField.getText());

        elegantMarketMail.setEmailSubject(mailSender.subTextField.getText());
        elegantMarketMail.setEmailMessage(mailSender.messageEditorPane.getText());
        emailList.add(elegantMarketMail);
        return emailList;
    }

    private void setEmailList() {
        BuySellManager buySellManager = new BuySellManager();
        FilterListModel flm = (FilterListModel) mailSender.toCCHelp.emailList.getModel();
        try {
            if (mailList == null || mailList.isEmpty()) {
                mailList = buySellManager.getGlobalMailList();
                int cnt = 1;
                for (ElegantMailList elegantMailList : mailList) {
                    flm.addElement(elegantMailList.getEmailAddress());
                    cnt++;
                }
                mailSender.toCCHelp.emailList.ensureIndexIsVisible(cnt);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return;
    }

    /**
     * @return the mailSender
     */
    public MailSender getMailSender() {
        return mailSender;
    }

    /**
     * @param mailSender the mailSender to set
     */
    public void setMailSender(MailSender mailSender) {
        this.mailSender = mailSender;
    }

    /**
     * @return the controlAction
     */
    public String getControlAction() {
        return controlAction;
    }

    /**
     * @param controlAction the controlAction to set
     */
    public void setControlAction(String controlAction) {
        this.controlAction = controlAction;
    }

}
