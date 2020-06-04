package org.farmas.model.game.phase;

import org.farmas.model.players.Player;
import org.farmas.model.questions.Level;
import org.farmas.model.questions.ListQuestions;
import org.farmas.model.themes.Themes;
import org.farmas.model.tools.RessourcesScanner;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class Phase2 implements Phase {

    public static int ID_PLAYER = 0;
    public static final int POINT_BY_QUESTION = 3;
    private static final int NB_OF_THEMES = 6;
    private static final int NB_OF_QUESTIONS = 6;
    private static final String LEVEL = Level.MEDIUM.toLowerCase();
    ArrayList<Player> players;
    ListQuestions listQuestions;
    String[] themes;

    public Phase2(ArrayList<Player> players, Themes themes) {
        ID_PLAYER = 0;
        this.players = players; // shallow copy
        this.listQuestions = new ListQuestions();
        this.themes = new String[NB_OF_THEMES];
        // pick themes
        selectThemes(themes);

        phaseDeJeu();

        listQuestions.forEach(System.out::println);

    }

    @Override
    public Player selectPlayer() {
        return this.players.get(ID_PLAYER);
    }

    @Override
    public void phaseDeJeu() {
        // load questions
        loadQuestionsFromJSON();
    }


    @Override
    public void selectThemes(Themes themes) {
        this.themes = themes.selectNRandomTheme(NB_OF_THEMES);
        for (String theme : this.themes) {
            System.out.println(theme);
        }
    }

    private void loadQuestionsFromJSON() {

        /*
            TODO | Mettre ça dans un thread
         */

        // list of JSON Files
        List<JSONObject> questionsFileArray = RessourcesScanner.readJSONFilesFromRessources("questions", themes);

        Map<JSONObject, String> selectedQuestions = new HashMap<>();
        if (questionsFileArray != null) {
            questionsFileArray.forEach(questionsFile -> {
                // get "results" array
                JSONArray results = (JSONArray) questionsFile.get("results");
                for (Object result : results) {
                    // cast each question block to JSONObject
                    JSONObject question = (JSONObject) result;

                    // get all medium questions
                    if (question.get("difficulty").equals(LEVEL))
                        selectedQuestions.put(question, question.get("category").toString());

                }
            });
        }

        // setup ListQuestions with the list of easy questions
        if (selectedQuestions.size() > 0) setupListQuestions(selectedQuestions);
        else System.out.println("No questions find ! :(");

    }

    private void setupListQuestions(Map<JSONObject, String> questionsByTheme) {
        //questionsByTheme.forEach((j, q) -> System.out.println(q));
        for (int i = 0; i < themes.length; i++) {
            int finalI = i;
            List<Map.Entry<JSONObject, String>> question = questionsByTheme.entrySet().stream().filter(q -> q.getValue().equals(themes[finalI])).collect(Collectors.toList());
            System.out.println(question.size());
            //int randomID = new Random().nextInt(question.size());
            //listQuestions.addQuestion(new Question<>(1, question.get(randomID).getKey()));
        }
    }
}
