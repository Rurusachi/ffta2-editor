package org.ruru.ffta2editor;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.ruru.ffta2editor.JobController.JobCell;
import org.ruru.ffta2editor.model.job.JobData;
import org.ruru.ffta2editor.model.job.JobGroup;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import org.ruru.ffta2editor.utility.AutoCompleteComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;

public class JobGroupController {
    
    private static Logger logger = Logger.getLogger("org.ruru.ffta2editor");
    
    public static class JobGroupCell extends ListCell<JobGroup> {
        Label label = new Label();

        public JobGroupCell() {
            label.setStyle("-fx-text-fill: black");
        }

        @Override protected void updateItem(JobGroup group, boolean empty) {
            super.updateItem(group, empty);
            if (group != null) {
                label.setText(String.format("%X: (%s, %s, %s, %s)", group.id, group.job1.getValue().name.getValue(),
                                                                              group.job2.getValue().name.getValue(),
                                                                              group.job3.getValue().name.getValue(), 
                                                                              group.job4.getValue().name.getValue()));
            } else {
                label.setText("");
            }
            setGraphic(label);
        }
    }

    @FXML ListView<JobGroup> jobGroupList;

    @FXML AutoCompleteComboBox<JobData> job1;
    @FXML AutoCompleteComboBox<JobData> job2;
    @FXML AutoCompleteComboBox<JobData> job3;
    @FXML AutoCompleteComboBox<JobData> job4;

    private ObjectProperty<JobGroup> jobGroupProperty = new SimpleObjectProperty<>();

    @FXML
    public void initialize() {
        
        jobGroupList.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            if (oldValue != null) unbindJobGroupData();
            jobGroupProperty.setValue(newValue);
            if (newValue != null) bindJobGroupData();
        });
    }

    private void unbindJobGroupData() {
        job1.valueProperty().unbindBidirectional(jobGroupProperty.getValue().job1);
        job2.valueProperty().unbindBidirectional(jobGroupProperty.getValue().job2);
        job3.valueProperty().unbindBidirectional(jobGroupProperty.getValue().job3);
        job4.valueProperty().unbindBidirectional(jobGroupProperty.getValue().job4);
    }

    private void bindJobGroupData() {
        job1.valueProperty().bindBidirectional(jobGroupProperty.getValue().job1);
        job2.valueProperty().bindBidirectional(jobGroupProperty.getValue().job2);
        job3.valueProperty().bindBidirectional(jobGroupProperty.getValue().job3);
        job4.valueProperty().bindBidirectional(jobGroupProperty.getValue().job4);
    }

    @FXML
    public void addJobGroup() {
        if (jobGroupList.getItems() != null) {
            int newIndex = jobGroupList.getItems().size();
            jobGroupList.getItems().add(new JobGroup(newIndex));
            jobGroupList.getSelectionModel().selectLast();;
        }
    }
    @FXML
    public void removeJobGroup() {
        //if (abilitySetList.getSelectionModel().getSelectedItem() != null && abilitySetList.getSelectionModel().getSelectedIndex() > 0) {
        //    abilitySetList.getItems().remove(abilitySetList.getSelectionModel().getSelectedIndex());
        //}
        if (jobGroupList.getItems().size() > 0) {
            jobGroupList.getItems().removeLast();
        }
    }


    public void loadJobGroups() throws Exception {
        if (App.archive != null) {

            ByteBuffer jobGroupBytes = App.sysdata.getFile(36);

            if (jobGroupBytes == null) {
                System.err.println("IdxAndPak null file error");
                throw new Exception("Job Group data is null");
            }
            jobGroupBytes.rewind();

            ObservableList<JobGroup> jobGroupDataList = FXCollections.observableArrayList();

            logger.info("Loading Job Groups");
            int numJobGroups = Byte.toUnsignedInt(App.arm9.get(0x000cb8b0))+1;
            for (int i = 0; i < numJobGroups; i++) {
                try {
                    JobGroup jobGroupData = new JobGroup(jobGroupBytes, i);
                    jobGroupDataList.add(jobGroupData);
                } catch (Exception e) {
                    logger.log(Level.SEVERE, String.format("Failed to load Job Group %d", i));
                    throw e;
                }
            }
            App.jobGroupList = jobGroupDataList;
            jobGroupList.setItems(jobGroupDataList);
            jobGroupList.setCellFactory(x -> new JobGroupCell());
        
            job1.setData(App.jobDataList);
            job1.setCellFactory(x -> new JobCell());
            job1.setButtonCell(new JobCell());

            job2.setData(App.jobDataList);
            job2.setCellFactory(x -> new JobCell());
            job2.setButtonCell(new JobCell());

            job3.setData(App.jobDataList);
            job3.setCellFactory(x -> new JobCell());
            job3.setButtonCell(new JobCell());

            job4.setData(App.jobDataList);
            job4.setCellFactory(x -> new JobCell());
            job4.setButtonCell(new JobCell());

            jobGroupBytes.rewind();
        }
    }

    public void saveJobGroups() {
        List<JobGroup> jobGroups = jobGroupList.getItems();
        ByteBuffer newJobGroupDatabytes = ByteBuffer.allocate(jobGroups.size()*0x4).order(ByteOrder.LITTLE_ENDIAN);

        logger.info("Saving Job Groups");
        for (int i = 0; i < jobGroups.size(); i++) {
            try {
                newJobGroupDatabytes.put(jobGroups.get(i).toBytes());
            } catch (Exception e) {
                logger.log(Level.SEVERE, String.format("Failed to save Job Group %d", i));
                throw e;
            }
        }
        newJobGroupDatabytes.rewind();
        App.sysdata.setFile(36, newJobGroupDatabytes);
        // Patch arm9 code with new Job length
        App.arm9.put(0x000cb8b0, (byte)(jobGroups.size()-1));
        App.arm9.put(0x000cb8b4, (byte)(jobGroups.size()-1));
    }
}
