package sample;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.text.Text;
import javafx.stage.Stage;

public class Controller {
    String[] usernamearray = {"user1", "user2", "user3"};
    String[] passwordarray = {"pass1", "pass2", "pass3"};
    int numUsers = 3;
    @FXML private Text errorText;
    @FXML private TextField username;
    @FXML private PasswordField password;
    boolean match = false;

    @FXML protected void handleSubmitButtonAction(ActionEvent event) {
        errorText.setText(username.getText()+" "+ password.getText());
        //go through database and find if usrname and password match anywhere
        for(int x=0;x<numUsers;x++){
            System.out.println( username.getText() + " "+ usernamearray[x]);
            System.out.println( password.getText() + " "+ passwordarray[x]);
            if(username.getText().equals(usernamearray[x]) && password.getText().equals(passwordarray[x])){
                match = true;
            }
        }
        if (match){
            //go to next screenParent root = null
            errorText.setText("good");
        }
        else{
            //show error message
            errorText.setText("Error: username or password incorrect");

        }
    }

}