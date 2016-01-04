package com.jugaado.jugaado.activities.settings;

import android.app.Activity;
import android.app.ExpandableListActivity;
import android.content.Context;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.widget.ExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.jugaado.jugaado.R;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by websofttechs on 12/1/2015.
 */
public class TermsNConditionActivity extends ExpandableListActivity {
    // Create ArrayList to hold parent Items and Child Items
    private ArrayList<String> parentItems = new ArrayList<String>();
    private ArrayList<Object> childItems = new ArrayList<Object>();

    @Override
    public void onCreate(Bundle savedInstanceState)
    {

        super.onCreate(savedInstanceState);

        //setContentView(R.layout.terms_n_condition);
        // Create Expandable List and set it's properties

        //RelativeLayout mainLayout=(RelativeLayout)findViewById(R.id.tnc_layout);

        ExpandableListView expandableList = getExpandableListView();
        expandableList.setDividerHeight(2);
        expandableList.setGroupIndicator(null);
        expandableList.setClickable(true);

        // Set the Items of Parent
        setGroupParents();
        // Set The Child Data
        setChildData();

        // Create the Adapter
        ExpandableCustomListAdapter adapter = new ExpandableCustomListAdapter(parentItems, childItems);

        adapter.setInflater((LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE), this);

        // Set the Adapter to expandableList
        expandableList.setAdapter(adapter);
        expandableList.setOnChildClickListener(this);

        //RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
        //params.addRule(RelativeLayout.ALIGN_PARENT_LEFT, RelativeLayout.TRUE);
        //params.addRule(RelativeLayout.ALIGN_PARENT_TOP, RelativeLayout.TRUE);
        //mainLayout.addView(expandableList, params);
    }

    // method to add parent Items
    public void setGroupParents()
    {
        parentItems.add("Your Agreement to Terms and Conditions for Availing Jugaado");
        parentItems.add("Eligibility");
        parentItems.add("Services");
        parentItems.add("Pricing Information");
        parentItems.add("User Obligations");
        parentItems.add("Use of Materials");
        parentItems.add("Usage Conduct");
        parentItems.add("Intellectual Property Rights");
        parentItems.add("Indemnification of Liability");
        parentItems.add("Violation of Terms of Use");
        parentItems.add("Governing Law");
    }

    // method to set child data of each parent
    public void setChildData()
    {

        // Add Child Items for "Your Agreement to Terms and Conditions for Availing Jugaado"
        ArrayList<String> child = new ArrayList<String>();
        child.add("The Terms and Conditions (as may be amended from time to time, the \"Agreement\") is a legal contract between you being, an individual customer, user, or beneficiary of this service of at least 18 years of age, and the Company. All services are rendered by the Company through its platform under the brand name 'Jugaado'. Hence all the rights, benefits, liabilities & obligations under the following terms & conditions shall accrue to the benefit of the Company (referred to as, \"us\", \"We\" or \"Jugaado\"), regarding your use of www.Jugaado.in or Services on Jugaado App or such other services which may be added from time to time (all such services are individually or collectively are referred as Service or Services as they case may be). Service can be used by you subject to your adherence with the terms and conditions set forth below including relevant policies. Jugaado reserves the right, at its sole discretion, to revise, add, or delete portions of these terms and conditions any time without further notice. You shall re-visit the \"Terms & Conditions\" link from time to time to stay abreast of any changes that the \"Site\" may introduce.");

        childItems.add(child);

        // Add Child Items for "Eligibility"
        child = new ArrayList<String>();
        child.add("The services are not available to persons under the age of 18 or to anyone previously suspended or removed from the services by Jugaado. By accepting these Terms & Conditions or by otherwise using the Services or the Site, You represent that You are at least 18 years of age and have not been previously suspended or removed from the Services. You represent and warrant that you have the right, authority, and capacity to enter into this Agreement and to abide by all of the terms and conditions of this Agreement. You shall not impersonate any person or entity, or falsely state or otherwise misrepresent identity, age or affiliation with any person or entity.");

        childItems.add(child);

        // Add Child Items for "Services"
        child = new ArrayList<String>();
        child.add("We are an Intermediary Only. Jugaado does not provide any service and is only a intermediary to services provided by other service providers, distributors or resellers. Jugaado is not a warrantor, insurer, or guarantor of the services to be provided by the service providers. Any disputes about the quality of service, its duration, specifications etc. must be communicated to the service provider directly.");

        childItems.add(child);

        // Add Child Items for "Pricing Information"
        child = new ArrayList<String>();
        child.add("Jugaado strives to provide You with the best prices possible on products and services You book or avail through the mobile app. The pricing details for purchase of products and services from the app are detailed under the Price / Cost Information. You agree to provide correct and accurate credit/ debit card details to the approved payment gateway for availing Services on the Website. You shall not use the credit/ debit card, which is not lawfully owned by You, i.e. in any transaction, You must use Your own credit/ debit card. The information provided by You will not be utilized or shared with any third party unless required in relation to fraud verifications or by law, regulation or court order. You will be solely responsible for the security and confidentiality of Your credit/ debit card details. Jugaado expressly disclaims all liabilities that may arise as a consequence of any unauthorized use of Your credit/ debit card. Jugaado shall under no liability whatsoever in respect of any loss or damage arising directly or indirectly out of the decline of authorization for any transaction, on account of you/cardholder having exceeded the preset limit mutually agreed to Jugaado with our acquiring bank from time to time.");

        childItems.add(child);

        // Add Child Items for "User Obligations"
        child = new ArrayList<String>();
        child.add("Subject to compliance with the Terms of Use, Jugaado grants You a non-exclusive, limited privilege to access and use the services provided therein. You agree to use the Services, Website and the services provided provided only for purposes that are permitted by: (a) the Terms of Use; and (b) any applicable law, regulation or generally accepted practices or guidelines in the relevant jurisdictions. You agree not to access (or attempt to access) the Services by any means other than through the interface that is provided by Jugaado. You shall not use any deep-link, robot, spider or other automatic device, program, algorithm or methodology, or any similar or equivalent manual process, to access, acquire, copy or monitor any portion of the Website or App (as defined below), or in any way reproduce or circumvent the navigational structure or presentation of the Website, materials or any Content, to obtain or attempt to obtain any materials, documents or information through any means not specifically made available through the service. You acknowledge and agree that by accessing or using the service, You may be exposed to content from other users that You may consider offensive, indecent or otherwise objectionable. Jugaado disclaims all liabilities arising in relation to such offensive content on the Website. Further, You may report such offensive content to Jugaado for appropriate action. You hereby undertake to ensure that any material shared by you is not offensive and in accordance with applicable laws. Further, You undertake not to: \n" +
                "Defame, abuse, harass, threaten or otherwise violate the legal rights of others\n" +
                "Impersonate any person or entity, or falsely state or otherwise misrepresent Your affiliation with a person or entity\n" +
                "Pass any inappropriate, profane, defamatory, infringing, obscene, indecent or unlawful topic, name, comments or remarks\n" +
                "Engage in any activity that interferes with or disrupts access to the service (or the servers and networks which are connected to the service)\n" +
                "Attempt to gain unauthorized access to any portion or feature of the service, any other systems or networks connected to the service, to any Jugaado server, by hacking, password mining or any other illegitimate means\n" +
                "Disrupt or interfere with the security of, or otherwise cause harm to, the service, systems resources, accounts, passwords, servers or networks connected to or accessible through the service\n" +
                "Use any device or software to interfere or attempt to interfere with the proper working of the service or any transaction being conducted on Jugaado\n" +
                "Use the Website or any material or Content for any purpose that is unlawful or prohibited by these Terms of Use, or to solicit the performance of any illegal activity or other activity which infringes the rights of Jugaado or other third parties\n" +
                "Violate any code of conduct or other guidelines, which may be applicable for or to any particular Service\n" +
                "Violate any applicable laws or regulations for the time being in force within or outside India\n" +
                "Violate the Terms of Use including but not limited to any applicable Additional Terms of the Website contained herein or elsewhere\n" +
                "Reverse engineer, modify, copy, distribute, transmit, display, perform, reproduce, publish, license, create derivative works from, transfer, or sell any information obtained from the Service\n" +
                "You agree that You are solely responsible to Jugaado and to any third party for any breach of Your obligations under the Terms of Use and for the consequences (including any loss or damage which Jugaado or its affiliates or its vendors may suffer) for any such breach. You agree and acknowledge that Jugaado is not the seller of the products and the services and Jugaado shall in no manner be deemed to be the seller of the products or services on this Website or App. Jugaado is only facilitating purchase of the products or services by You from the vendor by providing the Services to You. You agree that Jugaado may, at any time, modify or discontinue all or part of the service, charge, modify or waive fees required to use the service, or offer opportunities to some or all Users.");

        childItems.add(child);

        // Add Child Items for "Use of Materials"
        child = new ArrayList<String>();
        child.add("The rights granted to You to access / use information from the Service are not applicable to the design, layout or look and feel of the service. Such elements of the Website or App are protected by intellectual property rights and may not be copied or imitated in whole or in part. Any software that is available on the Website is the property of Jugaado. You may not use, download or install any software available at the Website, unless otherwise expressly permitted by these Terms of Use or by the express written permission of Jugaado.");
        childItems.add(child);

        // Add Child Items for "Usage Conduct"
        child = new ArrayList<String>();
        child.add("You shall solely be responsible for maintaining the necessary computer equipments and Internet connections that may be required to access, use and transact on the service. You are also under an obligation to use this service for reasonable and lawful purposes only, and shall not indulge in any activity that is not envisaged through the service.");
        childItems.add(child);
        // Add Child Items for "Intellectual Property Rights"
        child = new ArrayList<String>();
        child.add("The service and the processes, and their selection and arrangement, including but not limited to all text, graphics, user interfaces, visual interfaces, sounds and music (if any), artwork and computer code (collectively, the \"Content\") on the Service is owned and controlled by Jugaado and the design, structure, selection, coordination, expression, look and feel and arrangement of such Content is protected by copyright, patent and trademark laws, and various other intellectual property rights. The trademarks, logos and service marks displayed on the Website (\"Marks\") are the property of Jugaado or their vendors or respective third parties. You are not permitted to use the Marks without the prior consent of Jugaado, the vendor or the third party that may own the Marks. Unless otherwise indicated or anything contained to the contrary or any proprietary material owned by a third party and so expressly mentioned, the Company owns all intellectual property rights to and into the trademark \"Jugaado\", and the Website or App, including, without limitation, any and all rights, title and interest in and to copyright, related rights, patents, utility models, designs, know-how, trade secrets and inventions (patent pending), goodwill, source code, meta tags, databases, text, content, graphics, icons, and hyperlinks. Except as expressly provided herein, You acknowledge and agree that You shall not copy, republish, post, display, translate, transmit, reproduce or distribute any Content through any medium without obtaining the necessary authorization from Jugaado or thirty party owner of such Content.");

        childItems.add(child);

        // Add Child Items for "Indemnification of Liability"
        child = new ArrayList<String>();
        child.add("You agree to indemnify, defend and hold harmless Jugaado including but not limited to its affiliate vendors, agents and employees from and against any and all losses, liabilities, claims, damages, demands, costs and expenses (including legal fees and disbursements in connection therewith and interest chargeable thereon) asserted against or incurred by Jugaado that arise out of, result from, or may be payable by virtue of, any breach or non-performance of any representation, warranty, covenant or agreement made or obligation to be performed by You pursuant to these Terms of Use. Further, You agree to hold Jugaado harmless against any claims made by any third party due to, or arising out of, or in connection with, Your use of the Service, Your violation of the Terms of Use, or Your violation of any rights of another, including any intellectual property rights. Notwithstanding anything to contrary, Jugaado's entire liability to You under this Terms of Use or otherwise shall be the refund of the money charged from You for any specific voucher or product or service, under which the unlikely liability arises. In no event shall Jugaado, its officers, directors, employees or partners be liable to You, the vendor or any third party for any special, incidental, indirect, consequential or punitive damages whatsoever, including those resulting from loss of use, data or profits, whether or not foreseeable or whether or not Jugaado has been advised of the possibility of such damages, or based on any theory of liability, including breach of contract or warranty, negligence or other tortious action, or any other claim arising out of or in connection with Your use of or access to the Service. The limitations and exclusions in this section apply to the maximum extent permitted by applicable law.");

        childItems.add(child);

        // Add Child Items for "Violation of Terms of Use"
        child = new ArrayList<String>();
        child.add("You agree that Jugaado may, in its sole discretion and without prior notice, terminate Your access to the Service and block Your future access to the Service if Jugaado determines that You have violated these Terms of Use. You also agree that any violation by You of these Terms of Use will constitute an unlawful and unfair business practice, and will cause irreparable harm to Jugaado, for which monetary damages would be inadequate, and You consent to Jugaado obtaining any injunctive or equitable relief that Jugaado deems necessary or appropriate in such circumstances. These remedies are in addition to any other remedies Jugaado may have at law or in equity. You agree that Jugaado may, in its sole discretion, and without prior notice, terminate Your access to the Service, for cause, which includes (but is not limited to): (1) requests by law enforcement or other government agencies; (2) a request by You (self-initiated account deletions); (3) discontinuance or modification of the Service or any service offered on or through the Service; or (4) unexpected technical issues or problems. If Jugaado does take any legal action against You as a result of Your violation of these Terms of Use, Jugaado will be entitled to recover from You, and You agree to pay, all reasonable attorneys' fees and costs of such action, in addition to any other relief granted to Jugaado.");

        childItems.add(child);

        // Add Child Items for "Governing Law"
        child = new ArrayList<String>();
        child.add("These Terms of Use and all transactions entered into on or through the Service and the relationship between You and Jugaado shall be governed in accordance with the laws of India without reference to conflict of laws principles. You agree that all claims, differences and disputes arising under or in connection with or in relation hereto the Service, the Terms of Use or any transactions entered into on or through the Service or the relationship between You and Jugaado shall be subject to the exclusive jurisdiction of the courts at New Delhi, India and You hereby accede to and accept the jurisdiction of such courts.");

        childItems.add(child);
    }

}