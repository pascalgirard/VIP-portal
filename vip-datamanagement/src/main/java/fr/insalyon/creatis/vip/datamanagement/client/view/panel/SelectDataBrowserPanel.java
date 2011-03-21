/* Copyright CNRS-CREATIS
 *
 * Rafael Silva
 * rafael.silva@creatis.insa-lyon.fr
 * http://www.creatis.insa-lyon.fr/~silva
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
package fr.insalyon.creatis.vip.datamanagement.client.view.panel;

import com.gwtext.client.core.EventObject;
import com.gwtext.client.data.Record;
import com.gwtext.client.widgets.form.TextField;
import com.gwtext.client.widgets.grid.GridPanel;
import com.gwtext.client.widgets.grid.event.GridRowListenerAdapter;
import fr.insalyon.creatis.vip.datamanagement.client.DataManagerConstants;
import fr.insalyon.creatis.vip.datamanagement.client.view.window.SelectDataPathWindow;

/**
 *
 * @author Rafael Silva
 */
public class SelectDataBrowserPanel extends AbstractBrowserPanel {

    private static SelectDataBrowserPanel instance;

    public static SelectDataBrowserPanel getInstance() {
        if (instance == null) {
            instance = new SelectDataBrowserPanel();
        }
        return instance;
    }

    private SelectDataBrowserPanel() {
        super("dm-select");
        this.setWidth(450);
        this.configureGrid();
    }

    private void configureGrid() {
        grid.addGridRowListener(new GridRowListenerAdapter() {

            @Override
            public void onRowDblClick(GridPanel grid, int rowIndex, EventObject e) {
                Record record = grid.getStore().getRecordAt(rowIndex);
                String parentDir = pathCB.getValue();
                if (record.getAsString("typeico").equals("Folder")) {
                    String clickedFolderName = record.getAsString("fileName");
                    if (parentDir.equals(DataManagerConstants.ROOT)) {
                        parentDir = "";
                    }
                    loadData(parentDir + "/" + clickedFolderName, true);
                } else {
                    SelectDataPathWindow window = SelectDataPathWindow.getInstance();
                    TextField tf = (TextField) window.getFieldSet().findByID(window.getRefID());
                    tf.setValue(parentDir + "/" + record.getAsString("fileName"));
                    window.close();
                }
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        instance = null;
    }
}