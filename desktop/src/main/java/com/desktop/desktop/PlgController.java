package com.desktop.desktop;

import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ResourceBundle;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

public class PlgController implements Initializable {
    Stage stage;
    ObservableList<Pelanggan> listPlg = FXCollections.observableArrayList();
    boolean flagAdd = true;
    @FXML
    private Button bAdd;
    @FXML
    private Button bCancel;
    @FXML
    private Button bDel;
    @FXML
    private Button bEdit;
    @FXML
    private Button bUpdate;
    @FXML
    private TextField tfAlamat;
    @FXML
    private TextField tfNama;
    @FXML
    private TextField tfid;
    @FXML
    private TableColumn<Pelanggan, Integer> id;
    @FXML
    private TableColumn<Pelanggan, String> nama;
    @FXML
    private TableColumn<Pelanggan, String> alamat;
    @FXML
    private TableView<Pelanggan> tbPelanggan;
    @FXML
    private Button btnPilih;
    @FXML
    private TextField tfKeyword;

    @FXML
    void add(ActionEvent event) {
        setButton(false, false, false, true, true);
        clearTeks();
        setTeks(true);
        tfid.requestFocus();
    }

    @FXML
    void cancel(ActionEvent event) {
        setButton(true, true, true, false, false);
        clearTeks();
    }

    @FXML
    void del(ActionEvent event) {
        Connection conn = DBConnection.getConn();
        String sql = "DELETE FROM pelanggan WHERE idpelanggan=?";
        PreparedStatement st = null;
        try {
            st = conn.prepareStatement(sql);
            st.setString(1, tfid.getText());
            st.executeUpdate();
            loadData();
            clearTeks();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @FXML
    void edit(ActionEvent event) {
        flagAdd = false;
        setButton(false, false, false, true, true);
        setTeks(true);
        tfid.setEditable(false);
        tfNama.requestFocus();
    }

    @FXML
    void update(ActionEvent event) {
        Connection conn = DBConnection.getConn();
        if (flagAdd) {
            String sql = "INSERT INTO pelanggan (idpelanggan, nama, alamat) VALUES (?, ?, ?)";
            try {
                PreparedStatement st = conn.prepareStatement(sql);
                st.setString(1, tfid.getText());
                st.setString(2, tfNama.getText());
                st.setString(3, tfAlamat.getText());
                st.executeUpdate();
                loadData();
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        } else {
            String sql = "UPDATE pelanggan SET nama=?, alamat=? WHERE idpelanggan=?";
            try {
                PreparedStatement st = conn.prepareStatement(sql);
                st.setString(1, tfNama.getText());
                st.setString(2, tfAlamat.getText());
                st.setString(3, tfid.getText());
                st.executeUpdate();
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }
        loadData();
        setButton(true, true, true, false, false);
        clearTeks();
    }

    @FXML
    void pilih(ActionEvent event) {
        stage = (Stage) btnPilih.getScene().getWindow();
        Pelanggan p = tbPelanggan.getSelectionModel().getSelectedItem();
        stage.setUserData(p);
    }

    @FXML
    void pilihPelanggan(MouseEvent event) {
        Pelanggan p = tbPelanggan.getSelectionModel().getSelectedItem();
        tfid.setText(p.getId());
        tfNama.setText(p.getNama());
        tfAlamat.setText(p.getAlamat());
    }

    void initTabel() {
        id.setCellValueFactory(new PropertyValueFactory<Pelanggan, Integer>("id"));
        nama.setCellValueFactory(new PropertyValueFactory<Pelanggan, String>("nama"));
        alamat.setCellValueFactory(new PropertyValueFactory<Pelanggan, String>("alamat"));
    }

    void loadData() {
        listPlg = DBUtil.getDataPelanggan();
        tbPelanggan.setItems(listPlg);
    }

    void setFilter() {
        FilteredList<Pelanggan> filterData = new FilteredList<>(listPlg, b -> true);
        tfKeyword.textProperty().addListener((observable, oldValue, newValue) -> {
            filterData.setPredicate(p -> {
                if (newValue == null || newValue.isEmpty()) {
                    return true;
                }
                String searchKeyword = newValue.toLowerCase();
                if (p.getNama().toLowerCase().contains(searchKeyword)) {
                    return true;
                } else if (p.getId().toLowerCase().contains(searchKeyword)) {
                    return true;
                }
                return false;
            });
        });
        SortedList<Pelanggan> sortedData = new SortedList<>(filterData);
        sortedData.comparatorProperty().bind(tbPelanggan.comparatorProperty());
        tbPelanggan.setItems(sortedData);
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        initTabel();
        loadData();
        setFilter();
        setButton(true, true, true, false, false);
        setTeks(false);
    }

    protected void clearTeks() {
        tfid.setText(null);
        tfNama.setText(null);
        tfAlamat.setText(null);
    }

    protected void setButton(Boolean b1, Boolean b2, Boolean b3, Boolean b4, Boolean b5) {
        bAdd.setDisable(!b1);
        bEdit.setDisable(!b2);
        bDel.setDisable(!b3);
        bUpdate.setDisable(!b4);
        bCancel.setDisable(!b5);
    }

    protected void setTeks(Boolean b) {
        tfid.setEditable(b);
        tfNama.setEditable(b);
        tfAlamat.setEditable(b);
    }
}
