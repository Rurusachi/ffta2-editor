package org.ruru.ffta2editor;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.List;

import org.ruru.ffta2editor.AbilityController.AbilityCell;
import org.ruru.ffta2editor.EquipmentController.ItemCell;
import org.ruru.ffta2editor.FormationController.FormationCell;
import org.ruru.ffta2editor.JobController.JobCell;
import org.ruru.ffta2editor.TextController.StringPropertyCell;
import org.ruru.ffta2editor.model.ability.AbilityData;
import org.ruru.ffta2editor.model.formation.FormationData;
import org.ruru.ffta2editor.model.item.ItemData;
import org.ruru.ffta2editor.model.job.JobData;
import org.ruru.ffta2editor.model.quest.FFTA2Month;
import org.ruru.ffta2editor.model.quest.Quest;
import org.ruru.ffta2editor.utility.ByteChangeListener;
import org.ruru.ffta2editor.utility.IntRangeChangeListener;
import org.ruru.ffta2editor.utility.ShortChangeListener;
import org.ruru.ffta2editor.utility.UnsignedByteStringConverter;
import org.ruru.ffta2editor.utility.UnsignedShortStringConverter;

import javafx.beans.binding.Bindings;
import javafx.beans.binding.ObjectBinding;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.util.Pair;
import javafx.util.StringConverter;

public class QuestController {
    
    public static class QuestCell extends ListCell<Quest> {
        Label label = new Label();

        public QuestCell() {
            label.setStyle("-fx-text-fill: black");
        }

        @Override protected void updateItem(Quest quest, boolean empty) {
            super.updateItem(quest, empty);
            if (quest != null) {
                label.setText(String.format("%X: %s", quest.id, quest.name.getValue()));
            } else {
                label.setText("");
            }
            setGraphic(label);
        }
    }

    @FXML ListView<Quest> questList;

    @FXML TextField questName;
    @FXML TextArea questDescription;

    // Quest Info
    // ComboBox
    @FXML ComboBox<FormationData> formation;

    // Short
    @FXML TextField info_0x02;
    @FXML TextField startEvent;
    @FXML TextField endEvent;

    // Quest Data
    // ComboBox
    @FXML ComboBox<FFTA2Month> monthRequirement;
    @FXML ComboBox<AbilityData> abilityRequirement;
    @FXML ComboBox<ItemData> requiredItem1;
    @FXML ComboBox<ItemData> requiredItem2;
    @FXML ComboBox<ItemData> itemReward1;
    @FXML ComboBox<ItemData> itemReward2;
    @FXML ComboBox<ItemData> itemReward3;
    @FXML ComboBox<ItemData> itemReward4;
    @FXML ComboBox<StringProperty> questLocation;
    @FXML ComboBox<JobData> recommendedDispatch;

    // Short
    @FXML TextField storyRequirement;
    @FXML TextField flagRequirement;
    @FXML TextField unknownRequirementIndex;
    @FXML TextField battlefield;
    @FXML TextField gilReward;
    @FXML TextField _0x58;

    // Byte
    @FXML TextField type;
    @FXML TextField questGroup;
    @FXML TextField rank;
    @FXML TextField unknownRequirementValue;
    @FXML TextField dayRequirement;
    @FXML TextField _0x0d;
    @FXML TextField days;
    @FXML TextField fee;
    @FXML TextField negotiation;
    @FXML TextField aptitude;
    @FXML TextField teamwork;
    @FXML TextField adaptability;
    @FXML TextField _0x20;
    @FXML TextField _0x24;
    @FXML TextField _0x25;
    @FXML TextField _0x26;
    @FXML TextField _0x27;
    @FXML TextField _0x28;
    @FXML TextField _0x29;
    @FXML TextField _0x2b;
    @FXML TextField _0x2c;
    @FXML TextField _0x2d;
    @FXML TextField _0x2e;
    @FXML TextField _0x30;
    @FXML TextField _0x31;
    @FXML TextField _0x32;
    @FXML TextField _0x33;
    @FXML TextField _0x34;
    @FXML TextField _0x35;
    @FXML TextField _0x36;
    @FXML TextField _0x37;
    @FXML TextField _0x38;
    @FXML TextField _0x39;
    @FXML TextField _0x3a;
    @FXML TextField _0x3b;
    @FXML TextField _0x3c;
    @FXML TextField _0x3d;
    @FXML TextField _0x3e;
    @FXML TextField _0x3f;
    @FXML TextField _0x40;
    @FXML TextField _0x41;
    @FXML TextField _0x42;
    @FXML TextField _0x43;
    @FXML TextField _0x44;
    @FXML TextField _0x45;
    @FXML TextField _0x46;
    @FXML TextField _0x47;
    @FXML TextField _0x48;
    @FXML TextField _0x49;
    @FXML TextField _0x4a;
    @FXML TextField _0x4b;
    @FXML TextField apReward;
    @FXML TextField cpReward;
    @FXML TextField _0x5a;
    @FXML TextField _0x5b;
    @FXML TextField _0x5c;
    @FXML TextField _0x5d;
    @FXML TextField expReward;
    @FXML TextField _0x5f;
    @FXML TextField _0x60;
    @FXML TextField _0x61;
    @FXML TextField _0x62;
    @FXML TextField _0x63;
    @FXML TextField _0x17;
    @FXML TextField _0x1b;

    // Boolean
    @FXML CheckBox dispatch;

    @FXML CheckBox regionBit0;
    @FXML CheckBox targ;
    @FXML CheckBox camoa;
    @FXML CheckBox graszton;
    @FXML CheckBox moorabella;
    @FXML CheckBox fluorgis;
    @FXML CheckBox goug;
    @FXML CheckBox regionBit7;


    // Special handling
    @FXML TextField cooldownFailure;
    @FXML TextField cooldownSuccess;
    @FXML TextField requiredItemAmount1;
    @FXML TextField requiredItemAmount2;
    @FXML TextField itemRewardAmount1;
    @FXML TextField itemRewardAmount2;
    @FXML TextField itemRewardAmount3;
    @FXML TextField itemRewardAmount4;
    

    private ObjectProperty<Quest> questProperty = new SimpleObjectProperty<>();

    @FXML
    public void initialize() {
        questList.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            if (oldValue != null) unbindQuestData();
            questProperty.setValue(newValue);
            if (newValue != null) bindQuestData();
        });
        ObservableList<FFTA2Month> monthEnums = FXCollections.observableArrayList(FFTA2Month.values());
        monthRequirement.setItems(monthEnums);

        
        // Data validators
        storyRequirement.textProperty().addListener(new ShortChangeListener(storyRequirement));
        flagRequirement.textProperty().addListener(new ShortChangeListener(flagRequirement));
        unknownRequirementIndex.textProperty().addListener(new ShortChangeListener(unknownRequirementIndex));
        battlefield.textProperty().addListener(new ShortChangeListener(battlefield));
        gilReward.textProperty().addListener(new ShortChangeListener(gilReward));
        _0x58.textProperty().addListener(new ShortChangeListener(_0x58));

        type.textProperty().addListener(new ByteChangeListener(type));
        questGroup.textProperty().addListener(new ByteChangeListener(questGroup));
        rank.textProperty().addListener(new ByteChangeListener(rank));
        unknownRequirementValue.textProperty().addListener(new ByteChangeListener(unknownRequirementValue));
        _0x0d.textProperty().addListener(new ByteChangeListener(_0x0d));
        days.textProperty().addListener(new ByteChangeListener(days));
        fee.textProperty().addListener(new ByteChangeListener(fee));
        negotiation.textProperty().addListener(new ByteChangeListener(negotiation));
        aptitude.textProperty().addListener(new ByteChangeListener(aptitude));
        teamwork.textProperty().addListener(new ByteChangeListener(teamwork));
        adaptability.textProperty().addListener(new ByteChangeListener(adaptability));
        _0x20.textProperty().addListener(new ByteChangeListener(_0x20));
        _0x24.textProperty().addListener(new ByteChangeListener(_0x24));
        _0x25.textProperty().addListener(new ByteChangeListener(_0x25));
        _0x26.textProperty().addListener(new ByteChangeListener(_0x26));
        _0x27.textProperty().addListener(new ByteChangeListener(_0x27));
        _0x28.textProperty().addListener(new ByteChangeListener(_0x28));
        _0x29.textProperty().addListener(new ByteChangeListener(_0x29));
        _0x2b.textProperty().addListener(new ByteChangeListener(_0x2b));
        _0x2c.textProperty().addListener(new ByteChangeListener(_0x2c));
        _0x2d.textProperty().addListener(new ByteChangeListener(_0x2d));
        _0x2e.textProperty().addListener(new ByteChangeListener(_0x2e));
        _0x30.textProperty().addListener(new ByteChangeListener(_0x30));
        _0x31.textProperty().addListener(new ByteChangeListener(_0x31));
        _0x32.textProperty().addListener(new ByteChangeListener(_0x32));
        _0x33.textProperty().addListener(new ByteChangeListener(_0x33));
        _0x34.textProperty().addListener(new ByteChangeListener(_0x34));
        _0x35.textProperty().addListener(new ByteChangeListener(_0x35));
        _0x36.textProperty().addListener(new ByteChangeListener(_0x36));
        _0x37.textProperty().addListener(new ByteChangeListener(_0x37));
        _0x38.textProperty().addListener(new ByteChangeListener(_0x38));
        _0x39.textProperty().addListener(new ByteChangeListener(_0x39));
        _0x3a.textProperty().addListener(new ByteChangeListener(_0x3a));
        _0x3b.textProperty().addListener(new ByteChangeListener(_0x3b));
        _0x3c.textProperty().addListener(new ByteChangeListener(_0x3c));
        _0x3d.textProperty().addListener(new ByteChangeListener(_0x3d));
        _0x3e.textProperty().addListener(new ByteChangeListener(_0x3e));
        _0x3f.textProperty().addListener(new ByteChangeListener(_0x3f));
        _0x40.textProperty().addListener(new ByteChangeListener(_0x40));
        _0x41.textProperty().addListener(new ByteChangeListener(_0x41));
        _0x42.textProperty().addListener(new ByteChangeListener(_0x42));
        _0x43.textProperty().addListener(new ByteChangeListener(_0x43));
        _0x44.textProperty().addListener(new ByteChangeListener(_0x44));
        _0x45.textProperty().addListener(new ByteChangeListener(_0x45));
        _0x46.textProperty().addListener(new ByteChangeListener(_0x46));
        _0x47.textProperty().addListener(new ByteChangeListener(_0x47));
        _0x48.textProperty().addListener(new ByteChangeListener(_0x48));
        _0x49.textProperty().addListener(new ByteChangeListener(_0x49));
        _0x4a.textProperty().addListener(new ByteChangeListener(_0x4a));
        _0x4b.textProperty().addListener(new ByteChangeListener(_0x4b));
        apReward.textProperty().addListener(new ByteChangeListener(apReward));
        cpReward.textProperty().addListener(new ByteChangeListener(cpReward));
        _0x5a.textProperty().addListener(new ByteChangeListener(_0x5a));
        _0x5b.textProperty().addListener(new ByteChangeListener(_0x5b));
        _0x5c.textProperty().addListener(new ByteChangeListener(_0x5c));
        _0x5d.textProperty().addListener(new ByteChangeListener(_0x5d));
        expReward.textProperty().addListener(new ByteChangeListener(expReward));
        _0x5f.textProperty().addListener(new ByteChangeListener(_0x5f));
        _0x60.textProperty().addListener(new ByteChangeListener(_0x60));
        _0x61.textProperty().addListener(new ByteChangeListener(_0x61));
        _0x62.textProperty().addListener(new ByteChangeListener(_0x62));
        _0x63.textProperty().addListener(new ByteChangeListener(_0x63));
        _0x17.textProperty().addListener(new ByteChangeListener(_0x17));
        _0x1b.textProperty().addListener(new ByteChangeListener(_0x1b));

        
        dayRequirement.textProperty().addListener(new IntRangeChangeListener(dayRequirement, 0, 20));
        cooldownFailure.textProperty().addListener(new ByteChangeListener(cooldownFailure));
        cooldownSuccess.textProperty().addListener(new ByteChangeListener(cooldownSuccess));
        requiredItemAmount1.textProperty().addListener(new ByteChangeListener(requiredItemAmount1));
        requiredItemAmount2.textProperty().addListener(new ByteChangeListener(requiredItemAmount2));
        itemRewardAmount1.textProperty().addListener(new IntRangeChangeListener(itemRewardAmount1, 0, 63));
        itemRewardAmount2.textProperty().addListener(new IntRangeChangeListener(itemRewardAmount2, 0, 63));
        itemRewardAmount3.textProperty().addListener(new IntRangeChangeListener(itemRewardAmount3, 0, 63));
        itemRewardAmount4.textProperty().addListener(new IntRangeChangeListener(itemRewardAmount4, 0, 63));
    }

    private void unbindQuestData() {
        // Quest Info
        formation.valueProperty().unbindBidirectional(questProperty.getValue().info.formation);

        info_0x02.textProperty().unbindBidirectional(questProperty.getValue().info._0x02);
        startEvent.textProperty().unbindBidirectional(questProperty.getValue().info.startEvent);
        endEvent.textProperty().unbindBidirectional(questProperty.getValue().info.endEvent);

        // Quest Data
        monthRequirement.valueProperty().unbindBidirectional(questProperty.getValue().data.monthRequirement);
        abilityRequirement.valueProperty().unbindBidirectional(questProperty.getValue().data.abilityRequirement);
        requiredItem1.valueProperty().unbindBidirectional(questProperty.getValue().data.requiredItem1);
        requiredItem2.valueProperty().unbindBidirectional(questProperty.getValue().data.requiredItem2);
        itemReward1.valueProperty().unbindBidirectional(questProperty.getValue().data.itemReward1.item);
        itemReward2.valueProperty().unbindBidirectional(questProperty.getValue().data.itemReward2.item);
        itemReward3.valueProperty().unbindBidirectional(questProperty.getValue().data.itemReward3.item);
        itemReward4.valueProperty().unbindBidirectional(questProperty.getValue().data.itemReward4.item);
        recommendedDispatch.valueProperty().unbindBidirectional(questProperty.getValue().data.recommendedDispatch);
        
        questProperty.getValue().data.questLocation.unbind();

        storyRequirement.textProperty().unbindBidirectional(questProperty.getValue().data.storyRequirement);
        flagRequirement.textProperty().unbindBidirectional(questProperty.getValue().data.flagRequirement);
        unknownRequirementIndex.textProperty().unbindBidirectional(questProperty.getValue().data.unknownRequirementIndex);
        battlefield.textProperty().unbindBidirectional(questProperty.getValue().data.battlefield);
        gilReward.textProperty().unbindBidirectional(questProperty.getValue().data.gilReward);
        _0x58.textProperty().unbindBidirectional(questProperty.getValue().data._0x58);

        type.textProperty().unbindBidirectional(questProperty.getValue().data.type);
        questGroup.textProperty().unbindBidirectional(questProperty.getValue().data.questGroup);
        rank.textProperty().unbindBidirectional(questProperty.getValue().data.rank);
        unknownRequirementValue.textProperty().unbindBidirectional(questProperty.getValue().data.unknownRequirementValue);
        dayRequirement.textProperty().unbindBidirectional(questProperty.getValue().data.dayRequirement);
        _0x0d.textProperty().unbindBidirectional(questProperty.getValue().data._0x0d);
        days.textProperty().unbindBidirectional(questProperty.getValue().data.days);
        fee.textProperty().unbindBidirectional(questProperty.getValue().data.fee);
        negotiation.textProperty().unbindBidirectional(questProperty.getValue().data.negotiation);
        aptitude.textProperty().unbindBidirectional(questProperty.getValue().data.aptitude);
        teamwork.textProperty().unbindBidirectional(questProperty.getValue().data.teamwork);
        adaptability.textProperty().unbindBidirectional(questProperty.getValue().data.adaptability);
        _0x20.textProperty().unbindBidirectional(questProperty.getValue().data._0x20);
        _0x24.textProperty().unbindBidirectional(questProperty.getValue().data._0x24);
        _0x25.textProperty().unbindBidirectional(questProperty.getValue().data._0x25);
        _0x26.textProperty().unbindBidirectional(questProperty.getValue().data._0x26);
        _0x27.textProperty().unbindBidirectional(questProperty.getValue().data._0x27);
        _0x28.textProperty().unbindBidirectional(questProperty.getValue().data._0x28);
        _0x29.textProperty().unbindBidirectional(questProperty.getValue().data._0x29);
        _0x2b.textProperty().unbindBidirectional(questProperty.getValue().data._0x2b);
        _0x2c.textProperty().unbindBidirectional(questProperty.getValue().data._0x2c);
        _0x2d.textProperty().unbindBidirectional(questProperty.getValue().data._0x2d);
        _0x2e.textProperty().unbindBidirectional(questProperty.getValue().data._0x2e);
        _0x30.textProperty().unbindBidirectional(questProperty.getValue().data._0x30);
        _0x31.textProperty().unbindBidirectional(questProperty.getValue().data._0x31);
        _0x32.textProperty().unbindBidirectional(questProperty.getValue().data._0x32);
        _0x33.textProperty().unbindBidirectional(questProperty.getValue().data._0x33);
        _0x34.textProperty().unbindBidirectional(questProperty.getValue().data._0x34);
        _0x35.textProperty().unbindBidirectional(questProperty.getValue().data._0x35);
        _0x36.textProperty().unbindBidirectional(questProperty.getValue().data._0x36);
        _0x37.textProperty().unbindBidirectional(questProperty.getValue().data._0x37);
        _0x38.textProperty().unbindBidirectional(questProperty.getValue().data._0x38);
        _0x39.textProperty().unbindBidirectional(questProperty.getValue().data._0x39);
        _0x3a.textProperty().unbindBidirectional(questProperty.getValue().data._0x3a);
        _0x3b.textProperty().unbindBidirectional(questProperty.getValue().data._0x3b);
        _0x3c.textProperty().unbindBidirectional(questProperty.getValue().data._0x3c);
        _0x3d.textProperty().unbindBidirectional(questProperty.getValue().data._0x3d);
        _0x3e.textProperty().unbindBidirectional(questProperty.getValue().data._0x3e);
        _0x3f.textProperty().unbindBidirectional(questProperty.getValue().data._0x3f);
        _0x40.textProperty().unbindBidirectional(questProperty.getValue().data._0x40);
        _0x41.textProperty().unbindBidirectional(questProperty.getValue().data._0x41);
        _0x42.textProperty().unbindBidirectional(questProperty.getValue().data._0x42);
        _0x43.textProperty().unbindBidirectional(questProperty.getValue().data._0x43);
        _0x44.textProperty().unbindBidirectional(questProperty.getValue().data._0x44);
        _0x45.textProperty().unbindBidirectional(questProperty.getValue().data._0x45);
        _0x46.textProperty().unbindBidirectional(questProperty.getValue().data._0x46);
        _0x47.textProperty().unbindBidirectional(questProperty.getValue().data._0x47);
        _0x48.textProperty().unbindBidirectional(questProperty.getValue().data._0x48);
        _0x49.textProperty().unbindBidirectional(questProperty.getValue().data._0x49);
        _0x4a.textProperty().unbindBidirectional(questProperty.getValue().data._0x4a);
        _0x4b.textProperty().unbindBidirectional(questProperty.getValue().data._0x4b);
        apReward.textProperty().unbindBidirectional(questProperty.getValue().data.apReward);
        cpReward.textProperty().unbindBidirectional(questProperty.getValue().data.cpReward);
        _0x5a.textProperty().unbindBidirectional(questProperty.getValue().data._0x5a);
        _0x5b.textProperty().unbindBidirectional(questProperty.getValue().data._0x5b);
        _0x5c.textProperty().unbindBidirectional(questProperty.getValue().data._0x5c);
        _0x5d.textProperty().unbindBidirectional(questProperty.getValue().data._0x5d);
        expReward.textProperty().unbindBidirectional(questProperty.getValue().data.expReward);
        _0x5f.textProperty().unbindBidirectional(questProperty.getValue().data._0x5f);
        _0x60.textProperty().unbindBidirectional(questProperty.getValue().data._0x60);
        _0x61.textProperty().unbindBidirectional(questProperty.getValue().data._0x61);
        _0x62.textProperty().unbindBidirectional(questProperty.getValue().data._0x62);
        _0x63.textProperty().unbindBidirectional(questProperty.getValue().data._0x63);
        _0x17.textProperty().unbindBidirectional(questProperty.getValue().data._0x17);
        _0x1b.textProperty().unbindBidirectional(questProperty.getValue().data._0x1b);

        dispatch.selectedProperty().unbindBidirectional(questProperty.getValue().data.dispatch);
        regionBit0.selectedProperty().unbindBidirectional(questProperty.getValue().data.regionFlags.regionBit0);
        targ.selectedProperty().unbindBidirectional(questProperty.getValue().data.regionFlags.targ);
        camoa.selectedProperty().unbindBidirectional(questProperty.getValue().data.regionFlags.camoa);
        graszton.selectedProperty().unbindBidirectional(questProperty.getValue().data.regionFlags.graszton);
        moorabella.selectedProperty().unbindBidirectional(questProperty.getValue().data.regionFlags.moorabella);
        fluorgis.selectedProperty().unbindBidirectional(questProperty.getValue().data.regionFlags.fluorgis);
        goug.selectedProperty().unbindBidirectional(questProperty.getValue().data.regionFlags.goug);
        regionBit7.selectedProperty().unbindBidirectional(questProperty.getValue().data.regionFlags.regionBit7);

        cooldownFailure.textProperty().unbindBidirectional(questProperty.getValue().data.cooldownFailure);
        cooldownSuccess.textProperty().unbindBidirectional(questProperty.getValue().data.cooldownSuccess);
        requiredItemAmount1.textProperty().unbindBidirectional(questProperty.getValue().data.requiredItemAmount1);
        requiredItemAmount2.textProperty().unbindBidirectional(questProperty.getValue().data.requiredItemAmount2);
        itemRewardAmount1.textProperty().unbindBidirectional(questProperty.getValue().data.itemReward1.amount);
        itemRewardAmount2.textProperty().unbindBidirectional(questProperty.getValue().data.itemReward2.amount);
        itemRewardAmount3.textProperty().unbindBidirectional(questProperty.getValue().data.itemReward3.amount);
        itemRewardAmount4.textProperty().unbindBidirectional(questProperty.getValue().data.itemReward4.amount);

        questName.textProperty().unbindBidirectional(questProperty.getValue().name);
        questDescription.textProperty().unbindBidirectional(questProperty.getValue().description);
    }

    private void bindQuestData() {
        // Quest Info
        formation.valueProperty().bindBidirectional(questProperty.getValue().info.formation);
        
        StringConverter<Short> unsignedShortConverter = new UnsignedShortStringConverter();
        Bindings.bindBidirectional(info_0x02.textProperty(), questProperty.getValue().info._0x02, unsignedShortConverter);
        Bindings.bindBidirectional(startEvent.textProperty(), questProperty.getValue().info.startEvent, unsignedShortConverter);
        Bindings.bindBidirectional(endEvent.textProperty(), questProperty.getValue().info.endEvent, unsignedShortConverter);

        // Quest Data
        monthRequirement.valueProperty().bindBidirectional(questProperty.getValue().data.monthRequirement);
        abilityRequirement.valueProperty().bindBidirectional(questProperty.getValue().data.abilityRequirement);
        requiredItem1.valueProperty().bindBidirectional(questProperty.getValue().data.requiredItem1);
        requiredItem2.valueProperty().bindBidirectional(questProperty.getValue().data.requiredItem2);
        itemReward1.valueProperty().bindBidirectional(questProperty.getValue().data.itemReward1.item);
        itemReward2.valueProperty().bindBidirectional(questProperty.getValue().data.itemReward2.item);
        itemReward3.valueProperty().bindBidirectional(questProperty.getValue().data.itemReward3.item);
        itemReward4.valueProperty().bindBidirectional(questProperty.getValue().data.itemReward4.item);
        recommendedDispatch.valueProperty().bindBidirectional(questProperty.getValue().data.recommendedDispatch);

        questLocation.getSelectionModel().select(Byte.toUnsignedInt(questProperty.getValue().data.questLocation.getValue()));
        questProperty.getValue().data.questLocation.bind(new ObjectBinding<Byte>() {
            {bind(questLocation.valueProperty());}
            @Override
            protected Byte computeValue() {
                return (byte)App.locationNames.indexOf(questLocation.valueProperty().getValue());
            }

        });

        Bindings.bindBidirectional(storyRequirement.textProperty(), questProperty.getValue().data.storyRequirement, unsignedShortConverter);
        Bindings.bindBidirectional(flagRequirement.textProperty(), questProperty.getValue().data.flagRequirement, unsignedShortConverter);
        Bindings.bindBidirectional(unknownRequirementIndex.textProperty(), questProperty.getValue().data.unknownRequirementIndex, unsignedShortConverter);
        Bindings.bindBidirectional(battlefield.textProperty(), questProperty.getValue().data.battlefield, unsignedShortConverter);
        Bindings.bindBidirectional(gilReward.textProperty(), questProperty.getValue().data.gilReward, unsignedShortConverter);
        Bindings.bindBidirectional(_0x58.textProperty(), questProperty.getValue().data._0x58, unsignedShortConverter);

        StringConverter<Byte> unsignedByteConverter = new UnsignedByteStringConverter();
        Bindings.bindBidirectional(type.textProperty(), questProperty.getValue().data.type, unsignedByteConverter);
        Bindings.bindBidirectional(questGroup.textProperty(), questProperty.getValue().data.questGroup, unsignedByteConverter);
        Bindings.bindBidirectional(rank.textProperty(), questProperty.getValue().data.rank, unsignedByteConverter);
        Bindings.bindBidirectional(unknownRequirementValue.textProperty(), questProperty.getValue().data.unknownRequirementValue, unsignedByteConverter);
        Bindings.bindBidirectional(dayRequirement.textProperty(), questProperty.getValue().data.dayRequirement, unsignedByteConverter);
        Bindings.bindBidirectional(_0x0d.textProperty(), questProperty.getValue().data._0x0d, unsignedByteConverter);
        Bindings.bindBidirectional(days.textProperty(), questProperty.getValue().data.days, unsignedByteConverter);
        Bindings.bindBidirectional(fee.textProperty(), questProperty.getValue().data.fee, unsignedByteConverter);
        Bindings.bindBidirectional(negotiation.textProperty(), questProperty.getValue().data.negotiation, unsignedByteConverter);
        Bindings.bindBidirectional(aptitude.textProperty(), questProperty.getValue().data.aptitude, unsignedByteConverter);
        Bindings.bindBidirectional(teamwork.textProperty(), questProperty.getValue().data.teamwork, unsignedByteConverter);
        Bindings.bindBidirectional(adaptability.textProperty(), questProperty.getValue().data.adaptability, unsignedByteConverter);
        Bindings.bindBidirectional(_0x20.textProperty(), questProperty.getValue().data._0x20, unsignedByteConverter);
        Bindings.bindBidirectional(_0x24.textProperty(), questProperty.getValue().data._0x24, unsignedByteConverter);
        Bindings.bindBidirectional(_0x25.textProperty(), questProperty.getValue().data._0x25, unsignedByteConverter);
        Bindings.bindBidirectional(_0x26.textProperty(), questProperty.getValue().data._0x26, unsignedByteConverter);
        Bindings.bindBidirectional(_0x27.textProperty(), questProperty.getValue().data._0x27, unsignedByteConverter);
        Bindings.bindBidirectional(_0x28.textProperty(), questProperty.getValue().data._0x28, unsignedByteConverter);
        Bindings.bindBidirectional(_0x29.textProperty(), questProperty.getValue().data._0x29, unsignedByteConverter);
        Bindings.bindBidirectional(_0x2b.textProperty(), questProperty.getValue().data._0x2b, unsignedByteConverter);
        Bindings.bindBidirectional(_0x2c.textProperty(), questProperty.getValue().data._0x2c, unsignedByteConverter);
        Bindings.bindBidirectional(_0x2d.textProperty(), questProperty.getValue().data._0x2d, unsignedByteConverter);
        Bindings.bindBidirectional(_0x2e.textProperty(), questProperty.getValue().data._0x2e, unsignedByteConverter);
        Bindings.bindBidirectional(_0x30.textProperty(), questProperty.getValue().data._0x30, unsignedByteConverter);
        Bindings.bindBidirectional(_0x31.textProperty(), questProperty.getValue().data._0x31, unsignedByteConverter);
        Bindings.bindBidirectional(_0x32.textProperty(), questProperty.getValue().data._0x32, unsignedByteConverter);
        Bindings.bindBidirectional(_0x33.textProperty(), questProperty.getValue().data._0x33, unsignedByteConverter);
        Bindings.bindBidirectional(_0x34.textProperty(), questProperty.getValue().data._0x34, unsignedByteConverter);
        Bindings.bindBidirectional(_0x35.textProperty(), questProperty.getValue().data._0x35, unsignedByteConverter);
        Bindings.bindBidirectional(_0x36.textProperty(), questProperty.getValue().data._0x36, unsignedByteConverter);
        Bindings.bindBidirectional(_0x37.textProperty(), questProperty.getValue().data._0x37, unsignedByteConverter);
        Bindings.bindBidirectional(_0x38.textProperty(), questProperty.getValue().data._0x38, unsignedByteConverter);
        Bindings.bindBidirectional(_0x39.textProperty(), questProperty.getValue().data._0x39, unsignedByteConverter);
        Bindings.bindBidirectional(_0x3a.textProperty(), questProperty.getValue().data._0x3a, unsignedByteConverter);
        Bindings.bindBidirectional(_0x3b.textProperty(), questProperty.getValue().data._0x3b, unsignedByteConverter);
        Bindings.bindBidirectional(_0x3c.textProperty(), questProperty.getValue().data._0x3c, unsignedByteConverter);
        Bindings.bindBidirectional(_0x3d.textProperty(), questProperty.getValue().data._0x3d, unsignedByteConverter);
        Bindings.bindBidirectional(_0x3e.textProperty(), questProperty.getValue().data._0x3e, unsignedByteConverter);
        Bindings.bindBidirectional(_0x3f.textProperty(), questProperty.getValue().data._0x3f, unsignedByteConverter);
        Bindings.bindBidirectional(_0x40.textProperty(), questProperty.getValue().data._0x40, unsignedByteConverter);
        Bindings.bindBidirectional(_0x41.textProperty(), questProperty.getValue().data._0x41, unsignedByteConverter);
        Bindings.bindBidirectional(_0x42.textProperty(), questProperty.getValue().data._0x42, unsignedByteConverter);
        Bindings.bindBidirectional(_0x43.textProperty(), questProperty.getValue().data._0x43, unsignedByteConverter);
        Bindings.bindBidirectional(_0x44.textProperty(), questProperty.getValue().data._0x44, unsignedByteConverter);
        Bindings.bindBidirectional(_0x45.textProperty(), questProperty.getValue().data._0x45, unsignedByteConverter);
        Bindings.bindBidirectional(_0x46.textProperty(), questProperty.getValue().data._0x46, unsignedByteConverter);
        Bindings.bindBidirectional(_0x47.textProperty(), questProperty.getValue().data._0x47, unsignedByteConverter);
        Bindings.bindBidirectional(_0x48.textProperty(), questProperty.getValue().data._0x48, unsignedByteConverter);
        Bindings.bindBidirectional(_0x49.textProperty(), questProperty.getValue().data._0x49, unsignedByteConverter);
        Bindings.bindBidirectional(_0x4a.textProperty(), questProperty.getValue().data._0x4a, unsignedByteConverter);
        Bindings.bindBidirectional(_0x4b.textProperty(), questProperty.getValue().data._0x4b, unsignedByteConverter);
        Bindings.bindBidirectional(apReward.textProperty(), questProperty.getValue().data.apReward, unsignedByteConverter);
        Bindings.bindBidirectional(cpReward.textProperty(), questProperty.getValue().data.cpReward, unsignedByteConverter);
        Bindings.bindBidirectional(_0x5a.textProperty(), questProperty.getValue().data._0x5a, unsignedByteConverter);
        Bindings.bindBidirectional(_0x5b.textProperty(), questProperty.getValue().data._0x5b, unsignedByteConverter);
        Bindings.bindBidirectional(_0x5c.textProperty(), questProperty.getValue().data._0x5c, unsignedByteConverter);
        Bindings.bindBidirectional(_0x5d.textProperty(), questProperty.getValue().data._0x5d, unsignedByteConverter);
        Bindings.bindBidirectional(expReward.textProperty(), questProperty.getValue().data.expReward, unsignedByteConverter);
        Bindings.bindBidirectional(_0x5f.textProperty(), questProperty.getValue().data._0x5f, unsignedByteConverter);
        Bindings.bindBidirectional(_0x60.textProperty(), questProperty.getValue().data._0x60, unsignedByteConverter);
        Bindings.bindBidirectional(_0x61.textProperty(), questProperty.getValue().data._0x61, unsignedByteConverter);
        Bindings.bindBidirectional(_0x62.textProperty(), questProperty.getValue().data._0x62, unsignedByteConverter);
        Bindings.bindBidirectional(_0x63.textProperty(), questProperty.getValue().data._0x63, unsignedByteConverter);
        Bindings.bindBidirectional(_0x17.textProperty(), questProperty.getValue().data._0x17, unsignedByteConverter);
        Bindings.bindBidirectional(_0x1b.textProperty(), questProperty.getValue().data._0x1b, unsignedByteConverter);

        dispatch.selectedProperty().bindBidirectional(questProperty.getValue().data.dispatch);
        regionBit0.selectedProperty().bindBidirectional(questProperty.getValue().data.regionFlags.regionBit0);
        targ.selectedProperty().bindBidirectional(questProperty.getValue().data.regionFlags.targ);
        camoa.selectedProperty().bindBidirectional(questProperty.getValue().data.regionFlags.camoa);
        graszton.selectedProperty().bindBidirectional(questProperty.getValue().data.regionFlags.graszton);
        moorabella.selectedProperty().bindBidirectional(questProperty.getValue().data.regionFlags.moorabella);
        fluorgis.selectedProperty().bindBidirectional(questProperty.getValue().data.regionFlags.fluorgis);
        goug.selectedProperty().bindBidirectional(questProperty.getValue().data.regionFlags.goug);
        regionBit7.selectedProperty().bindBidirectional(questProperty.getValue().data.regionFlags.regionBit7);

        Bindings.bindBidirectional(cooldownFailure.textProperty(), questProperty.getValue().data.cooldownFailure, unsignedByteConverter);
        Bindings.bindBidirectional(cooldownSuccess.textProperty(), questProperty.getValue().data.cooldownSuccess, unsignedByteConverter);
        Bindings.bindBidirectional(requiredItemAmount1.textProperty(), questProperty.getValue().data.requiredItemAmount1, unsignedByteConverter);
        Bindings.bindBidirectional(requiredItemAmount2.textProperty(), questProperty.getValue().data.requiredItemAmount2, unsignedByteConverter);
        Bindings.bindBidirectional(itemRewardAmount1.textProperty(), questProperty.getValue().data.itemReward1.amount, unsignedByteConverter);
        Bindings.bindBidirectional(itemRewardAmount2.textProperty(), questProperty.getValue().data.itemReward2.amount, unsignedByteConverter);
        Bindings.bindBidirectional(itemRewardAmount3.textProperty(), questProperty.getValue().data.itemReward3.amount, unsignedByteConverter);
        Bindings.bindBidirectional(itemRewardAmount4.textProperty(), questProperty.getValue().data.itemReward4.amount, unsignedByteConverter);

        questName.textProperty().bindBidirectional(questProperty.getValue().name);
        questDescription.textProperty().bindBidirectional(questProperty.getValue().description);
    }

    @FXML
    public void addQuest() {
        if (questList.getItems() != null) {
            int newIndex = questList.getItems().size();
            questList.getItems().add(new Quest("", newIndex));
            questList.getSelectionModel().selectLast();
        }
    }
    @FXML
    public void removeQuest() {
        if (questList.getItems().size() > 0) {
            questList.getItems().removeLast();
        }
    }

    public void loadQuests() {
        if (App.archive != null) {

            ByteBuffer questInfoBytes = App.sysdata.getFile(10);
            ByteBuffer questDataBytes = App.sysdata.getFile(11);

            if (questInfoBytes == null || questDataBytes == null) {
                System.err.println("IdxAndPak null file error");
                return;
            }
            questInfoBytes.rewind();
            questDataBytes.rewind();

            ObservableList<Quest> questDataList = FXCollections.observableArrayList();

            int numQuestInfo = Short.toUnsignedInt(App.arm9.getShort(0x000cb284))+1;
            int numQuestData = Short.toUnsignedInt(App.arm9.getShort(0x000cb2c8))+1;
            if (numQuestData == numQuestInfo) {
                for (int i = 0; i < numQuestInfo; i++) {
                    Quest quest = new Quest(questInfoBytes, questDataBytes, i);
                    questDataList.add(quest);
                }
            } else {
                System.err.println("Quest info and Quest data don't match");
                return;
            }
            questList.setItems(questDataList);
            questList.setCellFactory(x -> new QuestCell());

            questInfoBytes.rewind();
            questDataBytes.rewind();


            formation.setItems(App.formationList);
            formation.setCellFactory(x -> new FormationCell());
            formation.setButtonCell(new FormationCell());

            abilityRequirement.setItems(App.abilityList);
            abilityRequirement.setCellFactory(x -> new AbilityCell<>());
            abilityRequirement.setButtonCell(new AbilityCell<>());
            
            requiredItem1.setItems(App.itemList);
            requiredItem1.setCellFactory(x -> new ItemCell<>());
            requiredItem1.setButtonCell(new ItemCell<>());
            
            requiredItem2.setItems(App.itemList);
            requiredItem2.setCellFactory(x -> new ItemCell<>());
            requiredItem2.setButtonCell(new ItemCell<>());
            
            itemReward1.setItems(App.itemList);
            itemReward1.setCellFactory(x -> new ItemCell<>());
            itemReward1.setButtonCell(new ItemCell<>());
            
            itemReward2.setItems(App.itemList);
            itemReward2.setCellFactory(x -> new ItemCell<>());
            itemReward2.setButtonCell(new ItemCell<>());
            
            itemReward3.setItems(App.itemList);
            itemReward3.setCellFactory(x -> new ItemCell<>());
            itemReward3.setButtonCell(new ItemCell<>());
            
            itemReward4.setItems(App.itemList);
            itemReward4.setCellFactory(x -> new ItemCell<>());
            itemReward4.setButtonCell(new ItemCell<>());

            recommendedDispatch.setItems(App.jobDataList);
            recommendedDispatch.setCellFactory(x -> new JobCell());
            recommendedDispatch.setButtonCell(new JobCell());

            questLocation.setItems(App.locationNames);
            questLocation.setCellFactory(x -> new StringPropertyCell());
            questLocation.setButtonCell(new StringPropertyCell());
        }
    }

    public void saveQuests() {
        List<Quest> quests = questList.getItems();
        ByteBuffer newQuestInfobytes = ByteBuffer.allocate(quests.size()*0x8).order(ByteOrder.LITTLE_ENDIAN);
        ByteBuffer newQuestDatabytes = ByteBuffer.allocate(quests.size()*0x64).order(ByteOrder.LITTLE_ENDIAN);

        for (int i = 0; i < quests.size(); i++) {
            Pair<byte[], byte[]> currQuestBytes = quests.get(i).toBytes();
            newQuestInfobytes.put(currQuestBytes.getKey());
            newQuestDatabytes.put(currQuestBytes.getValue());
        }
        newQuestInfobytes.rewind();
        newQuestDatabytes.rewind();
        App.sysdata.setFile(10, newQuestInfobytes);
        App.sysdata.setFile(11, newQuestDatabytes);
    }

}
