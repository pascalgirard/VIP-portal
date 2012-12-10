/* Copyright CNRS-CREATIS
 *
 * Rafael Ferreira da Silva
 * rafael.silva@creatis.insa-lyon.fr
 * http://www.rafaelsilva.com
 *
 * This software is a grid-enabled data-driven workflow manager and editor.
 *
 * This software is governed by the CeCILL  license under French law and
 * abiding by the rules of distribution of free software.  You can  use,
 * modify and/ or redistribute the software under the terms of the CeCILL
 * license as circulated by CEA, CNRS and INRIA at the following URL
 * "http://www.cecill.info".
 *
 * As a counterpart to the access to the source code and  rights to copy,
 * modify and redistribute granted by the license, users are provided only
 * with a limited warranty  and the software's author,  the holder of the
 * economic rights,  and the successive licensors  have only  limited
 * liability.
 *
 * In this respect, the user's attention is drawn to the risks associated
 * with loading,  using,  modifying and/or developing or reproducing the
 * software by the user in light of its specific status of free software,
 * that may mean  that it is complicated to manipulate,  and  that  also
 * therefore means  that it is reserved for developers  and  experienced
 * professionals having in-depth computer knowledge. Users are therefore
 * encouraged to load and test the software's suitability as regards their
 * requirements in conditions enabling the security of their systems and/or
 * data to be ensured and,  more generally, to use and operate it in the
 * same conditions as regards security.
 *
 * The fact that you are presently reading this means that you have had
 * knowledge of the CeCILL license and that you accept its terms.
 */
package fr.insalyon.creatis.vip.application.client.view.monitor.job;

import com.smartgwt.client.types.BkgndRepeat;
import com.smartgwt.client.types.Cursor;
import com.smartgwt.client.widgets.Label;
import com.smartgwt.client.widgets.events.ClickEvent;
import com.smartgwt.client.widgets.events.ClickHandler;
import com.smartgwt.client.widgets.events.MouseOutEvent;
import com.smartgwt.client.widgets.events.MouseOutHandler;
import com.smartgwt.client.widgets.events.MouseOverEvent;
import com.smartgwt.client.widgets.events.MouseOverHandler;
import com.smartgwt.client.widgets.layout.HLayout;
import com.smartgwt.client.widgets.layout.VLayout;
import fr.insalyon.creatis.vip.application.client.ApplicationConstants;
import fr.insalyon.creatis.vip.application.client.bean.Job;
import fr.insalyon.creatis.vip.core.client.view.util.WidgetUtil;

/**
 *
 * @author Rafael Ferreira da Silva
 */
public class JobLayout extends VLayout {

    private String simuID;
    private String command;
    private JobStatus status;
    private String parameters;
    private VLayout mainLayout;
    private Label statusLabel;
    private HLayout statusLayout;

    public JobLayout(final int jobID, String simulationID, Job job) {

        this.simuID = simulationID;
        this.command = job.getCommand();
        this.status = job.getStatus();
        this.parameters = job.getParameters();

        this.setWidth(70);
        this.setHeight(50);
        this.setBorder("1px solid #F2F2F2");
        this.setPadding(3);
        this.setMembersMargin(3);
        this.setBackgroundImage(ApplicationConstants.ICON_MONITOR_JOB);
        this.setBackgroundRepeat(BkgndRepeat.NO_REPEAT);
        this.setBackgroundPosition("center center");
        this.setCursor(Cursor.HAND);

        this.addClickHandler(new ClickHandler() {
            @Override
            public void onClick(ClickEvent event) {
                new DebugLayout(simuID, jobID, parameters, command).show();
            }
        });
        this.addMouseOverHandler(new MouseOverHandler() {
            @Override
            public void onMouseOver(MouseOverEvent event) {
                setBackgroundColor("#F7F7F7");
            }
        });
        this.addMouseOutHandler(new MouseOutHandler() {
            @Override
            public void onMouseOut(MouseOutEvent event) {
                setBackgroundColor("#FFFFFF");
            }
        });

        mainLayout = new VLayout();
        mainLayout.setWidth100();
        mainLayout.setHeight(40);
        this.addMember(mainLayout);

        Label jobIDLabel = WidgetUtil.getLabel("#" + jobID, 25, Cursor.HAND);
        mainLayout.addMember(jobIDLabel);

        statusLabel = WidgetUtil.getLabel("<b>" + status.name() + "</b>", 14, Cursor.HAND);
        mainLayout.addMember(statusLabel);

        statusLayout = new HLayout();
        statusLayout.setWidth100();
        statusLayout.setHeight(2);
        statusLayout.setBackgroundColor(status.getColor());

        this.addMember(statusLayout);
    }

//    /**
//     * Gives a color according to the job status.
//     *
//     * @return
//     */
//    private String parseStatusBackgroundColor() {
//
//        switch (status) {
//            case Queued:
//                return "#DBA400";
//            case Running:
//                return "#8CC653";
//            case Completed:
//                return "#287fd6";
//            default:
//                return "#d64949";
//        }
//    }

    public String getParameters() {
        return parameters;
    }

    public JobStatus getStatus() {
        return status;
    }

    public void updateStatus(JobStatus jobStatus) {

        if (status != jobStatus) {
            status = jobStatus;
            statusLabel.setContents("<b>" + status.name() + "</b>");
            statusLayout.setBackgroundColor(status.getColor());
        }
    }
}
