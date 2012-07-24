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
package fr.insalyon.creatis.vip.application.client.view.system.classes;

import com.google.gwt.user.client.rpc.AsyncCallback;
import com.smartgwt.client.types.MultipleAppearance;
import com.smartgwt.client.util.BooleanCallback;
import com.smartgwt.client.util.SC;
import com.smartgwt.client.widgets.IButton;
import com.smartgwt.client.widgets.events.ClickEvent;
import com.smartgwt.client.widgets.events.ClickHandler;
import com.smartgwt.client.widgets.form.fields.SelectItem;
import com.smartgwt.client.widgets.form.fields.TextItem;
import fr.insalyon.creatis.vip.application.client.ApplicationConstants;
import fr.insalyon.creatis.vip.application.client.bean.AppClass;
import fr.insalyon.creatis.vip.application.client.rpc.ApplicationService;
import fr.insalyon.creatis.vip.application.client.rpc.ApplicationServiceAsync;
import fr.insalyon.creatis.vip.core.client.bean.Group;
import fr.insalyon.creatis.vip.core.client.rpc.ConfigurationService;
import fr.insalyon.creatis.vip.core.client.rpc.ConfigurationServiceAsync;
import fr.insalyon.creatis.vip.core.client.view.CoreConstants;
import fr.insalyon.creatis.vip.core.client.view.common.AbstractFormLayout;
import fr.insalyon.creatis.vip.core.client.view.layout.Layout;
import fr.insalyon.creatis.vip.core.client.view.util.FieldUtil;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 *
 * @author Rafael Ferreira da Silva
 */
public class EditClassLayout extends AbstractFormLayout {

    private boolean newClass = true;
    private TextItem nameField;
    private SelectItem groupsPickList;
    private IButton removeButton;

    public EditClassLayout() {

        super(380, 200);
        addTitle("Add/Edit Class", ApplicationConstants.ICON_CLASSES);

        configure();
        loadData();
    }

    private void configure() {

        nameField = FieldUtil.getTextItem(350, null);

        groupsPickList = new SelectItem();
        groupsPickList.setShowTitle(false);
        groupsPickList.setMultiple(true);
        groupsPickList.setMultipleAppearance(MultipleAppearance.PICKLIST);
        groupsPickList.setWidth(350);

        IButton saveButton = new IButton("Save");
        saveButton.setIcon(CoreConstants.ICON_SAVE);
        saveButton.addClickHandler(new ClickHandler() {

            @Override
            public void onClick(ClickEvent event) {
                if (nameField.validate()) {
                    save(new AppClass(nameField.getValueAsString().trim(),
                            Arrays.asList(groupsPickList.getValues())));
                }
            }
        });

        removeButton = new IButton("Remove", new ClickHandler() {

            @Override
            public void onClick(ClickEvent event) {
                SC.ask("Do you really want to remove this class?", new BooleanCallback() {

                    @Override
                    public void execute(Boolean value) {
                        if (value) {
                            remove(nameField.getValueAsString().trim());
                        }
                    }
                });
            }
        });
        removeButton.setIcon(CoreConstants.ICON_DELETE);
        removeButton.setDisabled(true);

        addField("Name", nameField);
        addField("Groups", groupsPickList);
        addButtons(saveButton, removeButton);
    }

    /**
     * Sets a class to edit or creates a blank form.
     *
     * @param name Class name
     * @param groups Class groups
     */
    public void setClass(String name, String groups) {

        if (name != null) {
            this.nameField.setValue(name);
            this.nameField.setDisabled(true);
            this.groupsPickList.setValues(groups.split(", "));
            this.newClass = false;
            this.removeButton.setDisabled(false);

        } else {
            this.nameField.setValue("");
            this.nameField.setDisabled(false);
            this.groupsPickList.setValues(new String[]{});
            this.newClass = true;
            this.removeButton.setDisabled(true);
        }
    }

    /**
     *
     * @param appClass
     */
    private void save(AppClass appClass) {

        ApplicationServiceAsync service = ApplicationService.Util.getInstance();

        if (newClass) {
            modal.show("Adding class '" + appClass.getName() + "'...", true);
            service.addClass(appClass, getCallback("add"));

        } else {
            modal.show("Updating class '" + appClass.getName() + "'...", true);
            service.updateClass(appClass, getCallback("update"));
        }
    }

    /**
     *
     * @param text
     * @return
     */
    private AsyncCallback<Void> getCallback(final String text) {

        return new AsyncCallback<Void>() {

            @Override
            public void onFailure(Throwable caught) {
                modal.hide();
                SC.warn("Unable to " + text + " class:<br />" + caught.getMessage());
            }

            @Override
            public void onSuccess(Void result) {
                modal.hide();
                setClass(null, null);
                ManageClassesTab tab = (ManageClassesTab) Layout.getInstance().
                        getTab(ApplicationConstants.TAB_MANAGE_CLASSES);
                tab.loadClasses();
            }
        };
    }

    /**
     * Loads groups list
     */
    private void loadData() {

        ConfigurationServiceAsync service = ConfigurationService.Util.getInstance();
        final AsyncCallback<List<Group>> callback = new AsyncCallback<List<Group>>() {

            @Override
            public void onFailure(Throwable caught) {
                modal.hide();
                SC.warn("Unable to get list of groups:<br />" + caught.getMessage());
            }

            @Override
            public void onSuccess(List<Group> result) {
                modal.hide();
                List<String> dataList = new ArrayList<String>();
                for (Group group : result) {
                    dataList.add(group.getName());
                }
                groupsPickList.setValueMap(dataList.toArray(new String[]{}));
            }
        };
        modal.show("Loading groups...", true);
        service.getGroups(callback);
    }

    /**
     * Removes a class.
     *
     * @param name Class name
     */
    private void remove(String name) {

        ApplicationServiceAsync service = ApplicationService.Util.getInstance();
        final AsyncCallback<Void> callback = new AsyncCallback<Void>() {

            @Override
            public void onFailure(Throwable caught) {
                modal.hide();
                SC.warn("Unable to remove class:<br />" + caught.getMessage());
            }

            @Override
            public void onSuccess(Void result) {
                modal.hide();
                SC.say("The class was successfully removed!");
                ManageClassesTab tab = (ManageClassesTab) Layout.getInstance().
                        getTab(ApplicationConstants.TAB_MANAGE_CLASSES);
                tab.loadClasses();
            }
        };
        modal.show("Removing class '" + name + "'...", true);
        service.removeClass(name, callback);
    }
}