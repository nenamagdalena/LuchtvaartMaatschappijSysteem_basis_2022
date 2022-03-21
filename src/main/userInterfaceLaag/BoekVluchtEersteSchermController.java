package main.userInterfaceLaag;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import main.domeinLaag.Luchthaven;
import main.domeinLaag.LuchtvaartMaatschappij;
import main.domeinLaag.Vliegtuig;
import main.domeinLaag.Vlucht;

import java.net.URL;
import java.util.Calendar;
import java.util.ResourceBundle;
import java.util.Set;
import java.util.TreeMap;

public class BoekVluchtEersteSchermController implements Initializable {
    @FXML
    private ChoiceBox vertrekPuntChoiceBox;
    @FXML
    private DatePicker vertrekPuntDatepicker;
    @FXML
    private ChoiceBox bestemmingChoiceBox;
    @FXML
    public TableView<OverzichtVluchtenDataModel> tableView;
    @FXML
    TableColumn<OverzichtVluchtenDataModel, String> vliegtuigCol;
    @FXML
    TableColumn<OverzichtVluchtenDataModel, String> vertrekpuntCol;
    @FXML
    TableColumn<OverzichtVluchtenDataModel, String> bestemmingCol;
    @FXML
    TableColumn<OverzichtVluchtenDataModel, String> vertrekTijdCol;
    @FXML
    TableColumn<OverzichtVluchtenDataModel, String> aankomstTijdCol;
    @FXML
    private Button buttonSumbit;

    private static int hoogsteVluchtNummer = 1;
    private int vluchtNummer;
    private Vliegtuig vliegtuig;
    private Luchthaven bestemming;
    private Luchthaven vertrekpunt;
    private Calendar vertrekTijd;
    private Calendar aankomstTijd;
    private LuchtvaartMaatschappij lvm = LuchtvaartMaatschappij.getCurrentLuchtvaartMaatschappij();
    private ObservableList<OverzichtVluchtenDataModel> dataList;
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        fillDataList();
        vliegtuigCol.setCellValueFactory(new PropertyValueFactory<OverzichtVluchtenDataModel, String>("vliegtuigNm"));
        vertrekpuntCol.setCellValueFactory(new PropertyValueFactory<OverzichtVluchtenDataModel, String>("vertrekpunt"));
        bestemmingCol.setCellValueFactory(new PropertyValueFactory<OverzichtVluchtenDataModel, String>("bestemming"));
        vertrekTijdCol.setCellValueFactory(new PropertyValueFactory<OverzichtVluchtenDataModel, String>("vertrekTijd"));
        aankomstTijdCol.setCellValueFactory(new PropertyValueFactory<OverzichtVluchtenDataModel, String>("aankomstTijd"));
        tableView.getItems().setAll(dataList);

    }
    private void fillDataList() {
        dataList = FXCollections.observableArrayList();
        TreeMap<Integer, Vlucht> vluchten = Vlucht.geefAlle();
        Set<Integer> vluchtenSet = vluchten.keySet();
        for (Integer vluchtNr : vluchtenSet) {
            OverzichtVluchtenDataModel dataModel;
            Vlucht vlucht = vluchten.get(vluchtNr);
            Vliegtuig vt = vlucht.getVliegtuig();
            String vliegtuig = vt.geefNaam();
            String vertrekp = vlucht.getVertrekPunt().geefNaam();
            String bestemming = vlucht.getBestemming().geefNaam();
            Calendar vertrekTijd= vlucht.getVertrekTijd();
            Calendar aankomstTijd= vlucht.getAankomstTijd();
            dataModel = new OverzichtVluchtenDataModel(vliegtuig, vertrekp, bestemming, vertrekTijd, aankomstTijd);
            dataList.add(dataModel);
        }
    }
}
