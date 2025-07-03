package org.de190006.cinestar.cinestarpe_springboot.controllers;

import javafx.scene.layout.VBox;
import org.de190006.cinestar.cinestarpe_springboot.enums.Status;
import org.de190006.cinestar.cinestarpe_springboot.models.Account;
import org.de190006.cinestar.cinestarpe_springboot.models.Movie;
import org.de190006.cinestar.cinestarpe_springboot.models.Director;
import org.de190006.cinestar.cinestarpe_springboot.repositories.DirectorRepository;
import org.de190006.cinestar.cinestarpe_springboot.services.MovieService;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import java.util.List;

@Controller
public class MovieController {

    @FXML private TableView<Movie> moviesTable;
    @FXML private TextField movieNameField;
    @FXML private TextField durationField;
    @FXML private TextField actorField;
    @FXML private ComboBox<String> statusComboBox;
    @FXML private ComboBox<String> directorComboBox;
    @FXML private Label errorLabel;
    
    // Update form fields
    @FXML private TextField editMovieNameField;
    @FXML private TextField editDurationField;
    @FXML private TextField editActorField;
    @FXML private ComboBox<String> editStatusComboBox;
    @FXML private ComboBox<String> editDirectorComboBox;
    @FXML private VBox updateForm;
    
    // Search field
    @FXML private TextField searchField;
    @FXML private ComboBox<String> searchTypeComboBox;

    @Autowired
    private MovieService movieService;
    
    @Autowired
    private DirectorRepository directorRepository;
    
    private Account user; // Set this from your login logic

    private ObservableList<Movie> movieList;

    @FXML
    public void initialize() {
        // Initialize services, repositories, and UI controls
        movieList = FXCollections.observableArrayList();
        if (movieService != null) {
            movieList.setAll(movieService.getAllMovies());
        }
        moviesTable.setItems(movieList);
        
        // Initialize status combo boxes
        statusComboBox.setItems(FXCollections.observableArrayList("active", "inactive", "coming"));
        editStatusComboBox.setItems(FXCollections.observableArrayList("active", "inactive", "coming"));
        
        // Initialize director combo boxes (you'll need to populate these with actual director data)
        directorComboBox.setItems(FXCollections.observableArrayList());
        editDirectorComboBox.setItems(FXCollections.observableArrayList());
        
        // Populate director combo boxes with actual director data
        refreshDirectorList();
        
        // Add context menu for delete functionality
        ContextMenu contextMenu = new ContextMenu();
        MenuItem deleteItem = new MenuItem("Delete Movie");
        deleteItem.setOnAction(event -> onDeleteMovie(event));
        contextMenu.getItems().add(deleteItem);
        moviesTable.setContextMenu(contextMenu);
        
        // Add real-time search functionality
        if (searchField != null) {
            searchField.textProperty().addListener((observable, oldValue, newValue) -> {
                if (newValue != null && !newValue.trim().isEmpty()) {
                    performSearch(newValue.trim());
                } else {
                    refreshMovies();
                    showError("");
                }
            });
        }
        
        // Set default search type
        if (searchTypeComboBox != null) {
            searchTypeComboBox.setValue("Movie Name");
            
            // Add listener to update search field placeholder
            searchTypeComboBox.valueProperty().addListener((observable, oldValue, newValue) -> {
                if (newValue != null && searchField != null) {
                    switch (newValue) {
                        case "Movie Name":
                            searchField.setPromptText("Enter movie name...");
                            break;
                        case "Director":
                            searchField.setPromptText("Enter director name...");
                            break;
                        case "Actor":
                            searchField.setPromptText("Enter actor name...");
                            break;
                        case "Status":
                            searchField.setPromptText("Enter status (active/inactive/coming)...");
                            break;
                        default:
                            searchField.setPromptText("Enter search term...");
                            break;
                    }
                }
            });
        }
        
        if (errorLabel != null) {
            errorLabel.setText("");
            errorLabel.setVisible(false);
        }
    }

    @FXML
    private void onAddMovie(ActionEvent event) {
        if (!hasPermission(1)) return;
        
        try {
            String movieName = movieNameField.getText();
            int duration = Integer.parseInt(durationField.getText());
            String actor = actorField.getText();
            String status = statusComboBox.getValue();
            String selectedDirectorName = directorComboBox.getValue();
            
            if (selectedDirectorName == null || selectedDirectorName.trim().isEmpty()) {
                showError("Please select a director");
                return;
            }
            
            // Get director ID by name
            Integer directorId = getDirectorIdByName(selectedDirectorName);
            if (directorId == null) {
                showError("Selected director not found");
                return;
            }
            
            if (movieService != null) {
                String error = movieService.addMovie(movieName, duration, actor, Status.valueOf(status), directorId);
                if (error != null) {
                    showError(error);
                } else {
                    refreshMovies();
                    clearForm();
                    showError("Movie added successfully!");
                }
            }
        } catch (NumberFormatException e) {
            showError("Please enter a valid duration number");
        } catch (Exception e) {
            showError("Error adding movie: " + e.getMessage());
        }
    }

    @FXML
    private void onUpdateMovie(ActionEvent event) {
        if (!hasPermission(1)) return;
        
        Movie selected = moviesTable.getSelectionModel().getSelectedItem();
        if (selected == null) {
            showError("Please select a movie to update");
            return;
        }
        
        try {
            String movieName = editMovieNameField.getText();
            int duration = Integer.parseInt(editDurationField.getText());
            String actor = editActorField.getText();
            String status = editStatusComboBox.getValue();
            String selectedDirectorName = editDirectorComboBox.getValue();
            
            if (selectedDirectorName == null || selectedDirectorName.trim().isEmpty()) {
                showError("Please select a director");
                return;
            }
            
            // Get director ID by name
            Integer directorId = getDirectorIdByName(selectedDirectorName);
            if (directorId == null) {
                showError("Selected director not found");
                return;
            }
            
            if (movieService != null) {
                String error = movieService.updateMovie(selected.getMovieID(), movieName, duration, actor, Status.valueOf(status), directorId);
                if (error != null) {
                    showError(error);
                } else {
                    refreshMovies();
                    clearForm();
                    updateForm.setVisible(false);
                    showError("Movie updated successfully!");
                }
            }
        } catch (NumberFormatException e) {
            showError("Please enter a valid duration number");
        } catch (Exception e) {
            showError("Error updating movie: " + e.getMessage());
        }
    }

    @FXML
    private void onCancelUpdate(ActionEvent event) {
        updateForm.setVisible(false);
        clearForm();
    }

    @FXML
    private void onDeleteMovie(ActionEvent event) {
        if (!hasPermission(1)) return;
        
        Movie selected = moviesTable.getSelectionModel().getSelectedItem();
        if (selected != null) {
            if (movieService != null) {
                movieService.deleteMovie(selected.getMovieID());
                refreshMovies();
                clearForm();
                showError("Movie deleted successfully!");
            }
        } else {
            showError("Please select a movie to delete");
        }
    }

    @FXML
    private void onSearch(ActionEvent event) {
        if (!hasPermission(1, 3)) return;
        
        String searchTerm = searchField.getText();
        if (searchTerm != null && !searchTerm.trim().isEmpty()) {
            performSearch(searchTerm.trim());
        } else {
            refreshMovies();
            showError("Please enter a search term");
        }
    }

    @FXML
    private void onShowAll(ActionEvent event) {
        if (!hasPermission(1, 3)) return;
        
        searchField.clear();
        if (searchTypeComboBox != null) {
            searchTypeComboBox.setValue("Movie Name");
        }
        refreshMovies();
        showError("Showing all movies");
    }

    @FXML
    private void onTableClick(MouseEvent event) {
        Movie selected = moviesTable.getSelectionModel().getSelectedItem();
        if (selected != null) {
            // Check if it's a double-click for editing
            if (event.getClickCount() == 2) {
                editMovieNameField.setText(selected.getMovieName());
                editDurationField.setText(String.valueOf(selected.getDuration()));
                editActorField.setText(selected.getActor());
                editStatusComboBox.setValue(selected.getStatus().toString());
                
                // Set the director in the edit form
                if (selected.getDirector() != null) {
                    editDirectorComboBox.setValue(selected.getDirector().getDirectorName());
                }
                
                updateForm.setVisible(true);
            }
        }
    }

    private void refreshMovies() {
        if (movieService != null) {
            movieList.setAll(movieService.getAllMovies());
        }
        refreshDirectorList();
    }

    private void refreshDirectorList() {
        if (directorRepository != null) {
            try {
                List<Director> directors = directorRepository.findAll();
                ObservableList<String> directorNames = FXCollections.observableArrayList();
                for (Director director : directors) {
                    directorNames.add(director.getDirectorName());
                }
                directorComboBox.setItems(directorNames);
                editDirectorComboBox.setItems(directorNames);
            } catch (Exception e) {
                showError("Error refreshing director list: " + e.getMessage());
                e.printStackTrace();
            }
        }
    }

    private void clearForm() {
        if (movieNameField != null) movieNameField.clear();
        if (durationField != null) durationField.clear();
        if (actorField != null) actorField.clear();
        if (statusComboBox != null) statusComboBox.setValue(null);
        if (directorComboBox != null) directorComboBox.setValue(null);
        
        if (editMovieNameField != null) editMovieNameField.clear();
        if (editDurationField != null) editDurationField.clear();
        if (editActorField != null) editActorField.clear();
        if (editStatusComboBox != null) editStatusComboBox.setValue(null);
        if (editDirectorComboBox != null) editDirectorComboBox.setValue(null);
        
        showError("");
    }

    private boolean hasPermission(int... allowedRoles) {
        if (user == null) {
            showError("No user logged in.");
            return false;
        }
        for (int role : allowedRoles) {
            if (user.getRoleID() == role) return true;
        }
        showError("No permission.");
        return false;
    }
    
    private void showError(String message) {
        if (errorLabel != null) {
            errorLabel.setText(message);
            errorLabel.setVisible(message != null && !message.isEmpty());
            if (message != null && !message.isEmpty()) {
                errorLabel.setStyle("-fx-text-fill: " + (message.contains("successfully") ? "green" : "red"));
            }
        }
    }
    
    // Method to set the logged-in user
    public void setUser(Account user) {
        this.user = user;
    }
    
    private Integer getDirectorIdByName(String directorName) {
        if (directorRepository != null && directorName != null) {
            try {
                List<Director> directors = directorRepository.findAll();
                for (Director director : directors) {
                    if (director.getDirectorName().equals(directorName)) {
                        return director.getId();
                    }
                }
            } catch (Exception e) {
                showError("Error finding director: " + e.getMessage());
                e.printStackTrace();
            }
        }
        return null;
    }
    
    private void performSearch(String searchTerm) {
        if (!hasPermission(1, 3)) return;
        
        try {
            if (movieService != null) {
                List<Movie> searchResults;
                String searchType = searchTypeComboBox.getValue();
                
                if (searchType == null) {
                    searchType = "Movie Name"; // Default search type
                }
                
                switch (searchType) {
                    case "Movie Name":
                        searchResults = movieService.searchByMovieName(searchTerm);
                        break;
                    case "Director":
                        searchResults = movieService.searchByDirector(searchTerm);
                        break;
                    case "Actor":
                        searchResults = movieService.searchByActor(searchTerm);
                        break;
                    case "Status":
                        try {
                            Status status = Status.valueOf(searchTerm.toLowerCase());
                            searchResults = movieService.searchByStatus(status);
                        } catch (IllegalArgumentException e) {
                            showError("Invalid status. Use: active, inactive, or coming");
                            return;
                        }
                        break;
                    default:
                        searchResults = movieService.searchMovies(searchTerm);
                        break;
                }
                
                movieList.setAll(searchResults);
                
                if (searchResults.isEmpty()) {
                    showError("No movies found matching: " + searchTerm + " in " + searchType);
                } else {
                    showError("Found " + searchResults.size() + " movie(s) matching: " + searchTerm + " in " + searchType);
                }
            } else {
                showError("Movie service not available");
            }
        } catch (Exception e) {
            showError("Error during search: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
