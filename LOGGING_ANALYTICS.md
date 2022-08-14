# Logging analytics

## Wordle logging analytics

| Event name        | Triggered when...      | Parameters                                                                                                       |
|-------------------|------------------------|------------------------------------------------------------------------------------------------------------------|
| wordle_game_start | user starts a new game | wordle_word_length<br/>wordle_max_rows<br/>wordle_day (optional)                                                 |
| wordle_row_end    | user ends the game     | wordle_word_length<br/>wordle_max_rows<br/>wordle_last_row<br/>wordle_last_row_correct<br/>wordle_day (optional) |

### Parameters

| Parameter name          | Description                | Type   |
|-------------------------|----------------------------|--------|
| wordle_word_length      | Word length                | Number |
| wordle_max_rows         | Quiz max rows              | Number |
| wordle_day              | Wordle day mode: word date | String |
| wordle_last_row         | Quiz last row position     | Number |
| wordle_last_row_correct | is quiz last row correct   | String |