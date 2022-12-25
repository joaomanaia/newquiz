# Logging analytics

## Core logging analytics

| Event name  | Triggered when...                  | Parameters                        |
|-------------|------------------------------------|-----------------------------------|
| screen_view | This event signifies a screen view | screen_name                       |
| create_maze | User generates a new maze          | seed<br/>item_size<br/>game_modes |

### Parameters

| Parameter name | Description                 | Type   |
|----------------|-----------------------------|--------|
| screen_name    | Current screen name         | String |
| seed           | The seed of the maze        | Number |
| item_size      | The count of maze questions | Number |
| game_modes     | All maze game modes enabled | String |

## Multi choice logging analytics

| Event name              | Triggered when...      | Parameters                                                                                         |
|-------------------------|------------------------|----------------------------------------------------------------------------------------------------|
| multi_choice_game_start | user starts a new game | multi_choice_questions_size<br/>multi_choice_category<br/>multi_choice_difficulty<br/>maze_item_id |
| multi_choice_game_end   | user starts a new game | multi_choice_questions_size<br/>multi_choice_correct_answers<br/>maze_item_id                      |

### Parameters

| Parameter name               | Description                  | Type   |
|------------------------------|------------------------------|--------|
| multi_choice_questions_size  | Questions size               | Number |
| multi_choice_correct_answers | Correct answers size         | Number |
| multi_choice_category        | Category for all questions   | String |
| multi_choice_difficulty      | Difficulty for all questions | String |
| maze_item_id                 | Item id from maze game mode  | String |

## Wordle logging analytics

| Event name        | Triggered when...      | Parameters                                                                                                             |
|-------------------|------------------------|------------------------------------------------------------------------------------------------------------------------|
| wordle_game_start | user starts a new game | wordle_word_length<br/>wordle_max_rows<br/>wordle_day<br/>maze_item_id                                                 |
| wordle_row_end    | user ends the game     | wordle_word_length<br/>wordle_max_rows<br/>wordle_last_row<br/>wordle_last_row_correct<br/>wordle_day<br/>maze_item_id |

### Parameters

| Parameter name          | Description                 | Type   |
|-------------------------|-----------------------------|--------|
| wordle_word_length      | Word length                 | Number |
| wordle_max_rows         | Quiz max rows               | Number |
| wordle_day              | Wordle day mode: word date  | String |
| wordle_last_row         | Quiz last row position      | Number |
| wordle_last_row_correct | Is quiz last row correct    | String |
| wordle_quiz_type        | Type of wordle quiz         | String |
| maze_item_id            | Item id from maze game mode | String |