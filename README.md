# DiscussionBoard.java GUI Application

## Overview

`DiscussionBoard.java` is a Java-based GUI application designed to provide basic functionalities of a discussion board. Users can register themselves, create text posts, and search for posts by a specific user through a graphical interface.

---

## Features

1. **User Registration**:
   - Allows new users to register with a unique username.
   - Ensures username uniqueness to avoid duplication.

2. **Create Text Posts**:
   - Registered users can create text-based posts.
   - Each post is stored along with the username of the creator.

3. **Search Posts by User**:
   - Enables searching for all posts made by a specific user.
   - Displays the results in a user-friendly format.

---

## Prerequisites

- **Java Development Kit (JDK)**: Version 8 or higher.

---

## How to Run

1. Clone or download the project to your local machine.
2. Open the project in your preferred Java IDE.
3. Compile and run `DiscussionBoard.java`.

---

## Usage

### Main Interface

- Upon launching the application, you will see three main sections:
  1. **User Registration**
  2. **Post Creation**
  3. **Search for Posts**

### User Registration

- Enter a unique username in the "Register User" field.
- Click the "Register" button.
- A confirmation message will display if registration is successful.

### Create Text Post

- Enter your username in the "Username" field.
- Write your post in the "Post Content" field.
- Click the "Create Post" button to submit your post.
- A success message will confirm the post was created.

### Search for Posts by User

- Enter the username of the user whose posts you want to find in the "Search User" field.
- Click the "Search" button.
- A list of posts by the specified user will be displayed.

---

## GUI Components

1. **Text Fields**:
   - For username input during registration.
   - For writing post content.
   - For specifying the username to search posts.

2. **Buttons**:
   - **Register**: Registers a new user.
   - **Create Post**: Submits a text post.
   - **Search**: Searches for all posts by a given user.

3. **Display Area**:
   - Shows success or error messages.
   - Displays search results.

---

## Error Handling

- **User Registration**:
  - Displays an error message if the username is already taken.
- **Post Creation**:
  - Ensures the username exists before allowing post creation.
  - Displays an error if the post content is empty.
- **Search**:
  - Displays an error if the specified username does not exist.
  - Shows a message if no posts are found for the user.

---
