# Logging analytics

## Wordle logging analytics

| Event name           | Triggered when...                           | Parameters                                                                               |
|----------------------|---------------------------------------------|------------------------------------------------------------------------------------------|
| wordle_game_start    | user starts a new game                      | wordle_word_length<br/>wordle_max_rows<br/>wordle_day (optional)                         |
| wordle_row_end       | user ends the game                          | wordle_word_length<br/>wordle_max_rows<br/>wordle_day (optional)<br/>wordle_last_row     |
| wordle_row_completed | user completes and verifies the current row | wordle_word_length<br/>wordle_max_rows<br/>wordle_correct_items<br/>wordle_present_items |

### Parameters

| Parameter name          | Description                | Type   |
|-------------------------|----------------------------|--------|
| wordle_word_length      | Word length                | Number |
| wordle_max_rows         | Quiz max rows              | Number |
| wordle_day              | Wordle day mode: word date | String |
| wordle_correct_items    | Row correct items          | Number |
| wordle_present_items    | Row present items          | Number |
| wordle_last_row         | Quiz last row position     | Number |
| wordle_last_row_correct | is quiz last row correct   | String |