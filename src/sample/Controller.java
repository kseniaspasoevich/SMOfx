package sample;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import sample.dto.BufferDto;
import sample.dto.DeviceDto;
import sample.dto.ResultDto;
import sample.dto.SourceDto;
import javafx.scene.control.TableColumn;

import java.io.IOException;


public class Controller {
    @FXML
    private TextField buffSizeField;

    @FXML
    private TextField deviceSizeField;

    @FXML
    private TextField nTextField;

    @FXML
    private TextField sourceSizeField;

    @FXML
    private Button finishSimulationButton;

    @FXML
    private Label deviceAlphaLabel;
    @FXML
    private Label deviceBetaLabel;
    @FXML
    private Label  sourceLamLabel;
    @FXML
    private Label totalAmountLabel;
    @FXML
    private Label totalObrLabel;


    //tables in step mode
    ObservableList<SourceDto> sourcesData = FXCollections.observableArrayList();
    ObservableList<DeviceDto> deviceData = FXCollections.observableArrayList();
    ObservableList<BufferDto> bufferData = FXCollections.observableArrayList();

    //tables in automode
    ObservableList<SourceDto> sourceAutoData = FXCollections.observableArrayList();
    ObservableList<DeviceDto> deviceAutoData = FXCollections.observableArrayList();

    Integer deviceSize;
    Integer sourceSize;
    Integer n;
    Integer bufferSize;

    //---------------------------Table sources--------------------------------
    @FXML
    private TableView<SourceDto> sources;
    @FXML
    private TableColumn<SourceDto, Integer> iColumn;
    @FXML
    private TableColumn<SourceDto, Integer> idColumn;
    @FXML
    private TableColumn<SourceDto, Double> timeColumn;
    @FXML
    private TableColumn<SourceDto, Integer> refuseColumn;
    @FXML
    private TableColumn<SourceDto, Integer> totalColumn;

    //-------------------Table Devices---------------------------------------
    @FXML
    private TableView<DeviceDto> devices;
    @FXML
    private TableColumn<DeviceDto, Integer> noColumn2;
    @FXML
    private TableColumn<DeviceDto, Integer> reqIdColumn;
    @FXML
    private TableColumn<DeviceDto, Double> startTimeColumn;
    @FXML
    private TableColumn<DeviceDto, Double> endTimeColumn;
    @FXML
    private TableColumn<DeviceDto, Integer> amountColumn;
    @FXML
    private TableColumn<DeviceDto, Double> workTimeColumn;

    //----------------------------Table Buffers-------------------------------
    @FXML
    private TableView<BufferDto> buffers;
    @FXML
    private TableColumn<BufferDto, Integer> noColumn3;
    @FXML
    private TableColumn<BufferDto, Double> timeColumn3;
    @FXML
    private TableColumn<BufferDto, Integer> sourceColumn;
    @FXML
    private TableColumn<BufferDto, Integer> reqIdColumn3;

    //--------------------table Sources in automatic mode------------------------------------
    @FXML
    private TableView<SourceDto> sourcesAuto;
    @FXML
    private TableColumn<SourceDto, Integer> noSourceColumn;
    @FXML
    private TableColumn<SourceDto, Integer> idSourceColumn;
    @FXML
    private TableColumn<SourceDto, Double> timeAutoColumn;
    @FXML
    private TableColumn<SourceDto, Integer> refuseAutoColumn;
    @FXML
    private TableColumn<SourceDto, Integer> totalAutoColumn;
    @FXML
    private TableColumn<SourceDto, Double> pRefuseColumn;
    @FXML
    private TableColumn<SourceDto, Double> avgTimeSysColumn;
    @FXML
    private TableColumn<SourceDto, Double> avgWaitTimeSysColumn;
    @FXML
    private TableColumn<SourceDto, Double> avgProcTimeSysColumn;

    //-------------------table Devices in automatic mode-------------------------------------
    @FXML
    private TableView<DeviceDto> devicesAuto;
    @FXML
    private TableColumn<SourceDto, Integer> noDeviceColumn;
    @FXML
    private TableColumn<SourceDto, Integer> kUseColumn;



    private Stage stepModeStage;

    @FXML
    public void AutoModeClicked(ActionEvent actionEvent) {
        try {
            deviceSize = Integer.valueOf(deviceSizeField.getText());
            sourceSize = Integer.valueOf(sourceSizeField.getText());
            n = Integer.valueOf(nTextField.getText());
            bufferSize = Integer.valueOf(buffSizeField.getText());

            SimulationFx simulation = SimulationFx.getInstance();
            simulation.setBufSize(bufferSize);
            simulation.setDeviceSize(deviceSize);
            simulation.setN(n);
            simulation.setSourceAmount(sourceSize);
            simulation.stepAndAuto();

            Parent root = FXMLLoader.load(getClass().getResource("AutoMode.fxml"));
            Stage stage = new Stage();
            stage.setTitle("Auto Mode");
            stage.setScene(new Scene(root));
            stage.show();


        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    @FXML
    void showResultClicked(ActionEvent event) {


        SimulationFx simulation = SimulationFx.getInstance();

        ResultDto resultDto = simulation.printWholeResult();

        //sourcesAuto
        resultDto.getSourceDtoList().forEach(k -> sourceAutoData.add(k));
        noSourceColumn.setCellValueFactory(new PropertyValueFactory<SourceDto, Integer>("i"));
        idSourceColumn.setCellValueFactory(new PropertyValueFactory<SourceDto, Integer>("id"));
        timeAutoColumn.setCellValueFactory(new PropertyValueFactory<SourceDto, Double>("time"));
        refuseAutoColumn.setCellValueFactory(new PropertyValueFactory<SourceDto, Integer>("refuse"));
        totalAutoColumn.setCellValueFactory(new PropertyValueFactory<SourceDto, Integer>("amount"));
        pRefuseColumn.setCellValueFactory(new PropertyValueFactory<SourceDto, Double>("pRefuse"));
        avgTimeSysColumn.setCellValueFactory(new PropertyValueFactory<SourceDto, Double>("averageTimeInSystem"));
        avgWaitTimeSysColumn.setCellValueFactory(new PropertyValueFactory<SourceDto, Double>("averageWaitInSystem"));
        avgProcTimeSysColumn.setCellValueFactory(new PropertyValueFactory<SourceDto, Double>("avgProcTime"));
        sourcesAuto.setItems(sourceAutoData);

        //DevicesAuto
        resultDto.getDeviceDtoList().forEach(f -> deviceAutoData.add(f));
        noDeviceColumn.setCellValueFactory(new PropertyValueFactory<SourceDto, Integer>("i"));
        kUseColumn.setCellValueFactory(new PropertyValueFactory<SourceDto, Integer>("kUse"));
        devicesAuto.setItems(deviceAutoData);

        //filling labels
        deviceAlphaLabel.setText(String.valueOf(resultDto.getDeviceAlpha()));
        deviceBetaLabel.setText(String.valueOf(resultDto.getDeviceBeta()));
        sourceLamLabel.setText(String.valueOf(resultDto.getSourceLam()));
        totalAmountLabel.setText(String.valueOf(resultDto.getTotalAmount()));
        totalObrLabel.setText(String.valueOf(resultDto.getObr()+1));

    }


    @FXML
    public void stepModeClicked(ActionEvent actionEvent) {
        try {
            deviceSize = Integer.valueOf(deviceSizeField.getText());
            sourceSize = Integer.valueOf(sourceSizeField.getText());
            n = Integer.valueOf(nTextField.getText());
            bufferSize = Integer.valueOf(buffSizeField.getText());

            SimulationFx simulation = SimulationFx.getInstance();
            simulation.setBufSize(bufferSize);
            simulation.setDeviceSize(deviceSize);
            simulation.setN(n);
            simulation.setSourceAmount(sourceSize);
            simulation.stepAndAuto();

            Parent root = FXMLLoader.load(getClass().getResource("stepMode.fxml"));
            Stage stage = new Stage();
            stage.setTitle("Step Mode");
            stage.setScene(new Scene(root));
            stage.show();
            stepModeStage = stage;


        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    public void nextStepButton(ActionEvent actionEvent) {
        SimulationFx simulation = SimulationFx.getInstance();
        ResultDto resultDto = simulation.printStep();

        sourcesData.clear();
        resultDto.getSourceDtoList().forEach(v -> sourcesData.add(v));
        iColumn.setCellValueFactory(new PropertyValueFactory<SourceDto, Integer>("i"));
        idColumn.setCellValueFactory(new PropertyValueFactory<SourceDto, Integer>("id"));
        timeColumn.setCellValueFactory(new PropertyValueFactory<SourceDto, Double>("time"));
        refuseColumn.setCellValueFactory(new PropertyValueFactory<SourceDto, Integer>("refuse"));
        totalColumn.setCellValueFactory(new PropertyValueFactory<SourceDto, Integer>("amount"));
        sources.setItems(sourcesData);

        //devices
        deviceData.clear();
        resultDto.getDeviceDtoList().forEach(d -> deviceData.add(d));
        noColumn2.setCellValueFactory(new PropertyValueFactory<DeviceDto, Integer>("i"));
        reqIdColumn.setCellValueFactory(new PropertyValueFactory<DeviceDto, Integer>("id"));
        startTimeColumn.setCellValueFactory(new PropertyValueFactory<DeviceDto, Double>("startTime"));
        endTimeColumn.setCellValueFactory(new PropertyValueFactory<DeviceDto, Double>("time"));
        amountColumn.setCellValueFactory(new PropertyValueFactory<DeviceDto, Integer>("amount"));
        workTimeColumn.setCellValueFactory(new PropertyValueFactory<DeviceDto, Double>("workTime"));
        devices.setItems(deviceData);

        //buffers
        bufferData.clear();
        resultDto.getBufferDtoList().forEach(b -> bufferData.add(b));
        noColumn3.setCellValueFactory(new PropertyValueFactory<BufferDto, Integer>("i"));
        timeColumn3.setCellValueFactory(new PropertyValueFactory<BufferDto, Double>("time"));
        sourceColumn.setCellValueFactory(new PropertyValueFactory<BufferDto, Integer>("idSource"));
        reqIdColumn3.setCellValueFactory(new PropertyValueFactory<BufferDto, Integer>("id"));
        buffers.setItems(bufferData);

    }

    @FXML
    public void NClicked(ActionEvent actionEvent) {
        try {
            Parent root = FXMLLoader.load(getClass().getResource("buttonN.fxml"));
            SimulationFx simulation = SimulationFx.getInstance();
            simulation.findN();

            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Message Here...");
            alert.setHeaderText("Look, an Information Dialog");
            alert.setContentText(simulation.findN().toString());
            alert.showAndWait().ifPresent(rs -> {
                if (rs == ButtonType.OK) {
                    System.out.println("Pressed OK.");
                }
            });


        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void finishSimulationClicked(ActionEvent actionEvent) {
        try {
            ((Stage) finishSimulationButton.getScene().getWindow()).close();

            Parent root = FXMLLoader.load(getClass().getResource("AutoMode.fxml"));
            Stage stage = new Stage();
            stage.setTitle("Auto Mode after step mode");
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
