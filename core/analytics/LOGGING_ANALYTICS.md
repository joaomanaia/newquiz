# Overview

This document describes the logging analytics events and parameters, collected by the application. 
The events are sent anonymously to the Firebase Analytics. The user can opt out from the analytics in the settings screen.
These events are used to improve the application and to provide better user experience.

Firebase automatically collects some [events](https://support.google.com/firebase/answer/9234069?visit_id=638163118323661106-1839920987&rd=1).

# Core logging

## screen_view

This event logs the current user screen.

### Parameters

| Parameter name |  Type  | Required | Example value | Description |
|----------------|--------|----------|---------------|-------------|
| screen_name    | string | No       | Home Screen   | Screen name |

## level_up

This event logs the new level reached by the user.

### Parameters

| Parameter name |  Type  | Required | Example value | Description             |
|----------------|--------|----------|---------------|-------------------------|
| level          | number | Yes      | 10            | New level of the user   |
| character      | string | No       | global        | Where the user level up |

## earn_virtual_currency

This event logs the virtual currency earned by the user. This can be trigger when the user
reached a new level or when the user completed a quiz.

### Parameters

| Parameter name        |  Type  | Required | Example value | Description             |
|-----------------------|--------|----------|---------------|-------------------------|
| virtual_currency_name | string | Yes      | diamonds      | Virtual currency name   |
| value                 | number | Yes      | 10            | Virtual currency value  |

## spend_virtual_currency

This event logs the virtual currency spent by the user. This can be trigger when the user
buy a skips the question.

### Parameters

| Parameter name        |  Type  | Required | Example value | Description             |
|-----------------------|--------|----------|---------------|-------------------------|
| virtual_currency_name | string | Yes      | diamonds      | Virtual currency name   |
| value                 | number | Yes      | 10            | Virtual currency value  |

# Multi choice quiz

## multi_choice_game_start

This event logs the multi choice game quiz start.

### Parameters

| Parameter name              |  Type  | Required | Example value | Description                 |
|-----------------------------|--------|----------|---------------|-----------------------------|
| multi_choice_questions_size | number | Yes      | 10            | Questions size              |
| multi_choice_category       | string | No       | flag          | Category for questions      |
| multi_choice_difficulty     | string | No       | easy          | Difficulty for questions    |
| maze_item_id                | string | No       | 123456        | Item id from maze game mode |

## multi_choice_game_end

This event logs the multi choice game quiz end.

### Parameters

| Parameter name               |  Type  | Required | Example value | Description                 |
|------------------------------|--------|----------|---------------|-----------------------------|
| multi_choice_questions_size  | number | Yes      | 10            | Questions size              |
| multi_choice_correct_answers | number | Yes      | 5             | Correct answers size        |
| maze_item_id                 | string | No       | 123456        | Item id from maze game mode |

## multi_choice_category_clicked

This event logs when the user clicked on a multi choice quiz category.

### Parameters

| Parameter name | Type   | Required | Example value | Description     |
|----------------|--------|----------|---------------|-----------------|
| id             | string | Yes      | flag          | The category id |

## multi_choice_save_question

This event logs the user save a question from the multi choice quiz game.

## multi_choice_save_question

This event logs the user downloads multi choice quiz questions to save then.

## multi_choice_play_saved_questions

This event logs the user plays the saved multi choice quiz questions.

### Parameters

| Parameter name              | Type   | Required | Example value | Description    |
|-----------------------------|--------|----------|---------------|----------------|
| multi_choice_questions_size | number | Yes      | 10            | Questions size |

# Wordle logging analytics

## wordle_game_start

This event logs the wordle game quiz start.

### Parameters

| Parameter name     | Type   | Required | Example value | Description                 |
|--------------------|--------|----------|---------------|-----------------------------|
| wordle_word_length | number | Yes      | 5             | Word length                 |
| wordle_max_rows    | number | Yes      | 10            | Quiz max rows               |
| wordle_quiz_type   | string | No       | text          | The type of the wordle quiz |
| wordle_day         | string | No       | 2019-01-01    | Wordle day mode: word date  |
| maze_item_id       | string | No       | 123456        | Item id from maze game mode |

## wordle_game_end

This event logs the wordle game quiz end.

### Parameters

| Parameter name          | Type   | Required | Example value | Description                 |
|-------------------------|--------|----------|---------------|-----------------------------|
| wordle_word_length      | number | Yes      | 5             | Word length                 |
| wordle_max_rows         | number | Yes      | 10            | Quiz max rows               |
| wordle_last_row         | number | Yes      | 5             | Quiz last row position      |
| wordle_last_row_correct | string | Yes      | true          | Is quiz last row correct    |
| wordle_quiz_type        | string | No       | text          | The type of the wordle quiz |
| wordle_day              | string | No       | 2019-01-01    | Wordle day mode: word date  |
| maze_item_id            | string | No       | 123456        | Item id from maze game mode |

## daily_wordle_item_click

This event logs when the user click on a daily wordle item.

### Parameters

| Parameter name     | Type   | Required | Example value | Description                |
|--------------------|--------|----------|---------------|----------------------------|
| wordle_word_length | number | Yes      | 5             | Word length                |
| day                | string | Yes      | 2019-01-01    | Wordle day mode: word date |

## daily_wordle_item_complete

This event logs when the user complete a daily wordle item.

### Parameters

| Parameter name          | Type   | Required | Example value | Description                |
|-------------------------|--------|----------|---------------|----------------------------|
| wordle_word_length      | number | Yes      | 5             | Word length                |
| wordle_last_row_correct | string | Yes      | true          | Is quiz last row correct   |
| day                     | string | No       | 2019-01-01    | Wordle day mode: word date |

# Maze

## create_maze

This event logs the maze game mode creation.

### Parameters

| Parameter name |  Type  | Required | Example value | Description         |
|----------------|--------|----------|---------------|---------------------|
| seed           | number | No       | 123456        | Maze random seed    |
| item_size      | number | No       | 10            | Maze questions size |
| game_modes     | string | No       | wordle        | Maze game modes     |

## restart_maze

This event logs when the user restart the maze game mode.

## maze_item_click

This event logs when the user click on a maze item.

### Parameters

| Parameter name | Type   | Required | Example value | Description     |
|----------------|--------|----------|---------------|-----------------|
| item_index     | number | Yes      | 23            | Maze item index |

## maze_item_played

This event logs when the user play and ends the game of the maze item.

### Parameters

| Parameter name | Type   | Required | Example value | Description                     |
|----------------|--------|----------|---------------|---------------------------------|
| correct        | string | Yes      | true          | The user got the answer correct |

## maze_completed

This event logs when the user completes all the questions the maze game mode.

### Parameters

| Parameter name | Type   | Required | Example value | Description                     |
|----------------|--------|----------|---------------|---------------------------------|
| item_size      | number | Yes      | 23            | Number of questions in the maze |