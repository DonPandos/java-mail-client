package com.study.course4.emailclient.component;

import com.study.course4.emailclient.pojo.FolderPojo;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;

import java.io.IOException;

public class FolderListViewCell extends ListCell<FolderPojo> {
    @FXML
    private Label folderNameLabel;

    @FXML
    private ImageView folderIconImageView;

    @FXML
    private AnchorPane anchorPane;

    FXMLLoader fxmlLoader;

    @Override
    protected void updateItem(FolderPojo folder, boolean empty){
        super.updateItem(folder,empty);

        if(empty || folder == null){
            setText(null);
            setGraphic(null);
        }
        else {
            if(fxmlLoader == null){
                fxmlLoader = new FXMLLoader(getClass().getResource("../resources/view/folder_list_cell.fxml"));
                fxmlLoader.setController(this);

                try {
                    fxmlLoader.load();
                } catch (IOException e){
                    e.printStackTrace();
                }
            }
            folderNameLabel.setText(folder.getFolderName());
            Image image = new Image(getClass().getResource("../resources/img/" + folder.getImageURL()).toExternalForm());
            folderIconImageView.setImage(image);

            setText(null);
            setGraphic(anchorPane);
        }
    }
}
