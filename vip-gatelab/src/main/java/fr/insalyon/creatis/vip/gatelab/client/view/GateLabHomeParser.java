/* Copyright CNRS-CREATIS
 *
 * Rafael Silva
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
package fr.insalyon.creatis.vip.gatelab.client.view;

import fr.insalyon.creatis.vip.core.client.view.application.ApplicationParser;
import fr.insalyon.creatis.vip.core.client.view.layout.Layout;
import fr.insalyon.creatis.vip.gatelab.client.view.launch.GateLabLaunchTab;
import fr.insalyon.creatis.vip.gatelab.client.view.monitor.GateLabSimulationsTab;
import java.util.List;

/**
 *
 * @author Rafael Silva
 */
public class GateLabHomeParser extends ApplicationParser {

    private List<String> applicationNames;

    @Override
    public void loadApplications() {

        addApplication("GateLaunch", "img");
        addApplication("GateMonitor", "img");
        /*
        ApplicationServiceAsync service = ApplicationService.Util.getInstance();
        final AsyncCallback<List<Application>> callback = new AsyncCallback<List<Application>>() {

        public void onFailure(Throwable caught) {
        SC.say("Unable to load applications:<br />" + caught.getMessage());
        }

        public void onSuccess(List<Application> result) {

        List<ApplicationTileRecord> list = new ArrayList<ApplicationTileRecord>();
        applicationNames = new ArrayList<String>();
        addApplication(ApplicationConstants.APP_MONITOR,
        ApplicationConstants.APP_IMG_MONITOR);

        for (Application app : result) {
        addApplication(app.getName(), ApplicationConstants.APP_IMG_APPLICATION);
        applicationNames.add(app.getName());
        }
        }
        };
        service.getApplications(callback);
         *
         */
    }

    @Override
    public boolean parse(String applicationName) {

        if (applicationName.equals("GateMonitor")) {
            Layout.getInstance().addTab(new GateLabSimulationsTab());
            return true;
        }
        if (applicationName.equals("GateLaunch")) {
            Layout.getInstance().addTab(new GateLabLaunchTab("GateTest"));
            return true;
        }

        return false;
    }
}
