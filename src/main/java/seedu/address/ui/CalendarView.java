package seedu.address.ui;

import java.time.LocalDate;
import java.time.YearMonth;
import java.util.ArrayList;
import java.util.logging.Logger;

import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import seedu.address.commons.core.LogsCenter;
import seedu.address.model.task.Task;

/**
 * The Calendar of the app.
 */
public class CalendarView extends UiPart<Region> {

    private static final String FXML = "CalendarViewWithoutButtons.fxml";
    private final Logger logger = LogsCenter.getLogger(this.getClass());
    private ArrayList<CalendarNode> allCalendarDays = new ArrayList<>(35);
    private YearMonth currentYearMonth;
    private int[] numTasks = new int[31];
    private ObservableList<Task> tasks;
    private int currentMonth = 0;

    @FXML
    private VBox calendarVBox;
    @FXML
    private GridPane calendar;
    @FXML
    private Button previousMonth;
    @FXML
    private Button nextMonth;
    @FXML
    private Label calendarTitle;

    /**
     * Creates the calendar of the app
     */
    public CalendarView(ObservableList<Task> tasks) {
        super(FXML);
        this.tasks = tasks;
        YearMonth yearMonth = YearMonth.now();
        currentYearMonth = yearMonth;
        currentMonth = yearMonth.getMonthValue();
        setArray(tasks);
        initCalendar(yearMonth);
    }

    /**
     * Create rows and columns with anchor panes for the calendar
     */
    private void initCalendar(YearMonth yearMonth) {

        for (int i = 0; i < 5; i++) {
            for (int j = 0; j < 7; j++) {
                CalendarNode ap = new CalendarNode();
                ap.setPrefSize(300, 300);
                calendar.add(ap, j, i);
                allCalendarDays.add(ap);
            }
        }
        setCalendarDays(yearMonth);
    }

    /**
     * Updates numTask array.
     */
    public void setArray(ObservableList<Task> tasks) {
        tasks.forEach((task) -> {
            if (task.getDeadlineMonth() == currentYearMonth.getMonthValue()
                && task.getDeadlineYear() == currentYearMonth.getYear()) {
                numTasks[task.getDeadlineDay() - 1]++;
            }
        });
    }
    /**
     * Set the days of the calendar to display the correct date
     * @param yearMonth year and month of the current month
     */
    public void setCalendarDays(YearMonth yearMonth) {
        LocalDate calendarDate = LocalDate.of(yearMonth.getYear(), yearMonth.getMonthValue(), 1);
        while (!calendarDate.getDayOfWeek().toString().equals("SUNDAY")) {
            calendarDate = calendarDate.minusDays(1);
        }
        for (CalendarNode ap : allCalendarDays) {
            if (ap.getChildren().size() != 0) {
                ap.getChildren().remove(0);
            }
            Text txt;
            if (numTasks[calendarDate.getDayOfMonth() - 1] != 0) {
                txt = new Text(String.valueOf(calendarDate.getDayOfMonth()) + "\n\n"
                        + numTasks[calendarDate.getDayOfMonth() - 1] + " Tasks. ");
            } else {
                txt = new Text(String.valueOf(calendarDate.getDayOfMonth()));
            }
            ap.setDate(calendarDate);
            ap.setTopAnchor(txt, 5.0);
            ap.setLeftAnchor(txt, 5.0);
            ap.getChildren().add(txt);
            calendarDate = calendarDate.plusDays(1);
        }
        calendarTitle.setText(yearMonth.getMonth().toString() + " " + String.valueOf(yearMonth.getYear()));
    }

    /**
     * Refreshes the calendar with new information.
     */
    public void refreshCalendar() {
        initCalendar(currentYearMonth);
    }

    @FXML
    /**
     * Move the month back by one. Repopulate the calendar with the correct dates.
     */
    private void handlePreviousButtonAction() {
        currentYearMonth = currentYearMonth.minusMonths(1);
        currentMonth = currentYearMonth.getMonthValue();
        setCalendarDays(currentYearMonth);
    }

    @FXML
    /**
     * Move the month forward by one. Repopulate the calendar with the correct dates.
     */
    private void handleNextButtonAction() {
        currentYearMonth = currentYearMonth.plusMonths(1);
        currentMonth = currentYearMonth.getMonthValue();
        setCalendarDays(currentYearMonth);
    }

    public ArrayList<CalendarNode> getAllCalendarDays() {
        return allCalendarDays;
    }

    public void setAllCalendarDays(ArrayList<CalendarNode> allCalendarDays) {
        this.allCalendarDays = allCalendarDays;
    }
}

