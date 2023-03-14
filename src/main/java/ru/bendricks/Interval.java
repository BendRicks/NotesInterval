package ru.bendricks;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.regex.Pattern;

/**
 * Class for finding notes and intervals
 */
public class Interval {

    public static final String ASCENDING = "asc";
    public static final String DESCENDING = "dsc";

    private static final int MIN_ARGUMENTS = 2;
    private static final int MAX_ARGUMENTS = 3;

    private static final int CONSTRUCTION_INTERVAL_NAME_INDEX = 0;
    private static final int CONSTRUCTION_START_NOTE_INDEX = 1;

    private static final int INTERVAL_DEGREE_INDEX = 0;
    private static final int INTERVAL_SEMITONE_INDEX = 1;

    private static final int IDENTIFICATION_START_NOTE_INDEX = 0;
    private static final int IDENTIFICATION_END_NOTE_INDEX = 1;

    private static final int ASC_OR_DESC = 2;

    private static final List<Character> notes = new ArrayList<>(List.of('C', 'D', 'E', 'F', 'G', 'A', 'B'));
    private static final int NOTES_AMOUNT = 7;

    private static final char LOWER_SEMITONE_CHAR = 'b';
    private static final char RAISE_SEMITONE_CHAR = '#';

    private static final int[] afterNoteSemitones = new int[]{2,2,1,2,2,2,1};

    private static final Map<String, Integer[]> intervals = new HashMap<>(){{
       put("m2", new Integer[]{2, 1});
       put("M2", new Integer[]{2, 2});
       put("m3", new Integer[]{3, 3});
       put("M3", new Integer[]{3, 4});
       put("P4", new Integer[]{4, 5});
       put("P5", new Integer[]{5, 7});
       put("m6", new Integer[]{6, 8});
       put("M6", new Integer[]{6, 9});
       put("m7", new Integer[]{7, 10});
       put("M7", new Integer[]{7, 11});
       put("P8", new Integer[]{8, 12});
    }};

    /**
     * Finds note, that lays on specified interval from the start note
     * @param args Strings array, containing 2 or 3 elements:<br>
     *             args[1] - Interval name (m2, M2, m3, M3, P4, P5, m6, M6, m7, M7, P8)<br>
     *             args[2] - Start note (C, Ab, D#...)<br>
     *             args[3] - (Optional parameter) Indicates whether an interval is ascending or descending<br>
     *             (Interval.ASCENDING - "asc" or Interval.DESCENDING - "dsc")
     * @return Note name
     * @throws IllegalArgumentException validating args[]
     */
    public static String intervalConstruction(String[] args){
        validateIntervalConstruction(args);

        Integer[] interval = intervals.get(args[CONSTRUCTION_INTERVAL_NAME_INDEX]);
        boolean isDescending = args.length == MAX_ARGUMENTS && args[ASC_OR_DESC].equals(DESCENDING);
        int startNoteIndex = notes.indexOf(args[CONSTRUCTION_START_NOTE_INDEX].charAt(0));
        int intervalDegree = isDescending
                ? NOTES_AMOUNT - interval[INTERVAL_DEGREE_INDEX] + 1
                : interval[INTERVAL_DEGREE_INDEX] - 1;
        int endNoteIndex = (startNoteIndex + intervalDegree) % NOTES_AMOUNT;

        int semitonesSum = getSemitonesSum(isDescending, startNoteIndex, endNoteIndex);
        int semitonesSumCorrective = getSemitonesSumCorrective(args[CONSTRUCTION_START_NOTE_INDEX]);
        int semitoneDifference = interval[INTERVAL_SEMITONE_INDEX] - (semitonesSum + semitonesSumCorrective);
        StringBuilder resultNoteSB = new StringBuilder();
        resultNoteSB.append(notes.get(endNoteIndex));
        while (semitoneDifference != 0){
            if (semitoneDifference < 0){
                semitoneDifference++;
                resultNoteSB.append(LOWER_SEMITONE_CHAR);
            } else {
                semitoneDifference--;
                resultNoteSB.append(RAISE_SEMITONE_CHAR);
            }
        }
        return resultNoteSB.toString();
    }

    /**
     * Finds interval between start and end notes
     * @param args Strings array, containing 2 or 3 elements:<br>
     *             args[1] - Start note (C, Ab, D#...)<br>
     *             args[2] - End note (C, Ab, D#...)<br>
     *             args[3] - (Optional parameter) Indicates whether an interval is ascending or descending<br>
     *             (Interval.ASCENDING - "asc" or Interval.DESCENDING - "dsc")
     * @return Interval name
     * @throws IllegalArgumentException validating args[]
     * @throws RuntimeException If there's no such interval
     */
    public static String intervalIdentification(String[] args){
        validateIntervalIdentification(args);

        boolean isDescending = args.length == MAX_ARGUMENTS && args[ASC_OR_DESC].equals(DESCENDING);
        int startNoteIndex = notes.indexOf(args[IDENTIFICATION_START_NOTE_INDEX].charAt(0));
        int endNoteIndex = notes.indexOf(args[IDENTIFICATION_END_NOTE_INDEX].charAt(0));

        int intervalDegree =
                (NOTES_AMOUNT - (isDescending ? (endNoteIndex - startNoteIndex) : (startNoteIndex - endNoteIndex))) % 7;
        intervalDegree = intervalDegree == 0 ? 8 : intervalDegree + 1;
        int semitonesSum = getSemitonesSum(isDescending, startNoteIndex, endNoteIndex);
        int intervalSemitones = semitonesSum
                + getSemitonesSumCorrective(args[IDENTIFICATION_START_NOTE_INDEX])
                - getSemitonesSumCorrective(args[IDENTIFICATION_END_NOTE_INDEX]);
        int finalIntervalDegree = intervalDegree;
        Optional<Map.Entry<String, Integer[]>> interval = intervals.entrySet().stream().filter(item -> item.getValue()[INTERVAL_DEGREE_INDEX] == finalIntervalDegree
                && item.getValue()[INTERVAL_SEMITONE_INDEX] == intervalSemitones).findAny();
        if (interval.isEmpty()){
            throw new RuntimeException("Cannot identify the interval");
        }
        return interval.get().getKey();
    }

    /**
     * Validates args[] for intervalConstruction()
     * @param args Params from intervalConstruction()
     */
    private static void validateIntervalConstruction(String[] args){
        basicArrayValidation(args);
        checkNoteSyntax(args[CONSTRUCTION_START_NOTE_INDEX]);
        if (!notes.contains(args[CONSTRUCTION_START_NOTE_INDEX].charAt(0))){
            throw new IllegalArgumentException("Cannot identify start note");
        }
        if (!intervals.containsKey(args[CONSTRUCTION_INTERVAL_NAME_INDEX])){
            throw new IllegalArgumentException("Cannot identify the interval");
        }
    }

    /**
     * Validates args[] for intervalIdentification()
     * @param args Params from intervalIdentification()
     */
    private static void validateIntervalIdentification(String[] args){
        basicArrayValidation(args);
        checkNoteSyntax(args[IDENTIFICATION_START_NOTE_INDEX]);
        checkNoteSyntax(args[IDENTIFICATION_END_NOTE_INDEX]);
        if (!notes.contains(args[IDENTIFICATION_START_NOTE_INDEX].charAt(0))){
            throw new IllegalArgumentException("Cannot identify start note");
        }
        if (!notes.contains(args[IDENTIFICATION_END_NOTE_INDEX].charAt(0))){
            throw new IllegalArgumentException("Cannot identify end note");
        }
    }

    /**
     * Gets corrective in semitones for notes with raising or lowering characters ('#' or 'b')
     * @param note Note string
     * @return Corrective in int
     */
    private static int getSemitonesSumCorrective(String note) {
        int semitonesSumCorrective = 0;
        for (int i = 1; i < note.length(); i++) {
            char currChar = note.charAt(i);
            if (currChar == LOWER_SEMITONE_CHAR) {
                semitonesSumCorrective++;
            } else if (currChar == RAISE_SEMITONE_CHAR) {
                semitonesSumCorrective--;
            }
        }
        return semitonesSumCorrective;
    }

    /**
     * Gets sum of all semitones between 2 simple notes ('C','D','E','F'...)
     * @param isDescending Indicates whether an interval is ascending or descending
     * @param startNoteIndex Index of starting note
     * @param endNoteIndex Index of ending note
     * @return Sum of semitones
     */
    private static int getSemitonesSum(boolean isDescending, int startNoteIndex, int endNoteIndex) {
        int currNoteIndex = isDescending
                ? startNoteIndex -1
                : startNoteIndex;
        int semitonesSum = 0;
        int indexModifier = isDescending ? 6 : 1;
        do {
            semitonesSum += afterNoteSemitones[currNoteIndex];
            currNoteIndex = (currNoteIndex + indexModifier) % NOTES_AMOUNT;
        } while ((!isDescending && currNoteIndex != endNoteIndex) || (isDescending && currNoteIndex != (endNoteIndex + 6) % 7));
        return semitonesSum;
    }

    /**
     * Basic validation for all arrays. Checks if array is not null, if it matches by size and if any of its parameters is null
     * @param args Array to validate
     * @throws IllegalArgumentException If validation fails
     */
    private static void basicArrayValidation(String[] args){
        if (args == null){
            throw new IllegalArgumentException("Passed array is null");
        }
        checkArraySizeMatchesRequirements(args);
        checkArrayElementsNotNull(args);
    }

    /**
     * Checks if array matches by size
     * @param args Array to check
     */
    private static void checkArraySizeMatchesRequirements(String[] args){
        if (args.length < MIN_ARGUMENTS || args.length > MAX_ARGUMENTS){
            throw new IllegalArgumentException("Illegal number of elements in input array");
        }
    }

    /**
     * Checks if array elements are not null
     * @param args Array to check
     */
    private static void checkArrayElementsNotNull(String[] args){
        for (String str : args){
            if (str == null){
                throw new IllegalArgumentException("There are null arguments in passed array");
            }
        }
    }

    /**
     * Checks note string for syntax errors
     * @param note Note to check
     */
    private static void checkNoteSyntax(String note){
        String regex = "[CDEFGAB](bb|##|b|#)?";
        if (!Pattern.matches(regex, note)){
            throw new IllegalArgumentException("Note " + note + " failed verification");
        }
    }

}