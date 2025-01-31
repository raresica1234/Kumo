#!/bin/bash

SESSION_NAME="kumo"

# Check if the session already exists
tmux has-session -t $SESSION_NAME 2>/dev/null
if [ $? != 0 ]; then
  # Create a new session
  tmux new-session -d -s $SESSION_NAME -n backend

  # Create additional windows if needed
  tmux new-window -t $SESSION_NAME -n frontend
  tmux new-window -t $SESSION_NAME -n run

  # Optionally, send commands to panes
  tmux send-keys -t $SESSION_NAME:0 "cd backend; vim ." C-m
  tmux send-keys -t $SESSION_NAME:1 "cd frontend; vim ." C-m
  tmux send-keys -t $SESSION_NAME:2 "cd frontend; ng serve" C-m

  # Switch to the first window
  tmux select-window -t $SESSION_NAME:0
fi

# Attach to the session
tmux attach -t $SESSION_NAME

