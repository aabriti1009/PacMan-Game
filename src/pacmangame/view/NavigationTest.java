package pacmangame.view;

/**
 * Simple test class to demonstrate navigation between LoginView and RegistrationView
 */
public class NavigationTest {
    
    public static void main(String[] args) {
        // Start with LoginView
        System.out.println("=== PACMAN GAME NAVIGATION TEST ===");
        System.out.println("Starting LoginView...");
        LoginView loginView = new LoginView();
        
        // The LoginView will be visible and when you click "Create Account",
        // it will open RegistrationView
        System.out.println("LoginView is now visible.");
        System.out.println("Click 'Create Account' to open RegistrationView.");
        System.out.println("Click 'Sign In' in RegistrationView to go back to LoginView.");
        System.out.println("Complete navigation flow:");
        System.out.println("LoginView ↔ RegistrationView ↔ MainMenu");
        System.out.println("==========================================");
    }
} 