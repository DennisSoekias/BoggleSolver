import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.NavigableSet;
import java.util.TreeSet;

import javax.swing.JScrollPane;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.stage.Stage;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextArea;
import javafx.geometry.Pos;
import javafx.geometry.Insets;

public class BoggleGui extends Application {
	
	static char Board[][] = {{'a', 'q', 'b', 'a' },
						     {'n', 'w', 'c', 'q' },
						     {'k', 'x', 'i', 'w' },
						     {'e', 'f', 'g', 'x' },};
	
    private static final NavigableSet<String> dictionary;
    private List<String> list = solve(Board);

    static {
        dictionary = new TreeSet<String>();
        try {
            FileReader file = new FileReader("src/dict.txt");
            BufferedReader read = new BufferedReader(file);
            String line;
            while ((line = read.readLine()) != null) {
                dictionary.add(line.split(":")[0]);
            }
        } catch (Exception e) {
            throw new RuntimeException("Dictionary not found!");
        }
    }

    public static List<String> solve(char[][] m) {
        if (m == null) {
            throw new NullPointerException("");
        }
        final List<String> words = new ArrayList<String>();
        for (int i = 0; i < m.length; i++) {
            for (int j = 0; j < m[0].length; j++) {
                solve(m, i, j, m[i][j] + "", words);
            }
        }
        return words;
    }

    private static void solve(char[][] m, int i, int j, String prefix, List<String> words) {
        assert m != null;
        assert words != null;

        for (int x = Math.max(0, i - 1); x < Math.min(m.length, i + 2); x++) {
            for (int f = Math.max(0, j - 1); f < Math.min(m[0].length, j + 2); f++) {
                if (x == i && f == j) continue;

                String word = prefix+ m[x][f];
                if (!dictionary.subSet(word, word + Character.MAX_VALUE).isEmpty()) {
                    if (dictionary.contains(word)) {
                        words.add(word);
                    }
                    solve(m, x, f, word, words);
                }
                
            }
        } 
    }

	public static void main(String[] args) {
		launch(args);
	}
	
	@Override
	public void start(Stage primaryStage) throws Exception {
		primaryStage.setTitle("Boggle Solver");
		
		BorderPane pane2 = new BorderPane();
		GridPane board = new GridPane();
		board.setPadding(new Insets(25));
		
		TextArea textArea = new TextArea();
	    VBox vbox = new VBox(textArea);
	    vbox.setAlignment(Pos.BOTTOM_CENTER);
	    ScrollPane scrollPane = new ScrollPane(textArea);
	    
	    String newLine = System.getProperty("line.separator");

	    textArea.appendText("FOUND WORDS:");
	    textArea.appendText(newLine);
	    textArea.setEditable(false);

	    
		for (String str :  list) {
		    textArea.appendText(str);
		    textArea.appendText(newLine);
        }

		Label label1 = new Label("Boggle Board");
		label1.setFont(Font.font("Arial", FontWeight.BOLD, 20));

		for (int i = 0 ; i < 4 ; i++) {
			for (int j = 0 ; j < 4 ; j++) {
				Label label = new Label(Board[i][j] + "");
				GridPane.setConstraints(label, i, j);
				label.setFont(new Font(20));
				label.setPadding(new Insets(15, 25, 15, 25));
				board.getChildren().add(label);
			}	
		}
		
		board.setAlignment(Pos.TOP_CENTER);
		pane2.setAlignment(label1, Pos.TOP_CENTER);
		pane2.setBottom(scrollPane);
		pane2.setCenter(board);
		pane2.setTop(label1);
	
		Scene scene = new Scene(pane2, 400, 400);
		primaryStage.setScene(scene);
		primaryStage.show();

	}
}