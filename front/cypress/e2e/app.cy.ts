describe('AppComponent', () => {

  it('should log out the user', () => {
    cy.intercept('GET', 'api/session', {
      id: 100,
      name: 'Morning Yoga',
      description: 'A relaxing session',
      date: '2025-01-15T00:00:00.000+00:00',
      teacher_id: 11,
      users: [],
      createdAt: '2025-01-01T10:00:26',
      updatedAt: '2025-01-01T10:00:26'
    }).as('getSessions');

    cy.visit('/sessions', {
      onBeforeLoad(win) {
        win.CypressLoggedIn = true;
        win.isAdmin = true;
      }
    });
    cy.wait('@getSessions');

    // Clique sur logout
    cy.contains('Logout').click();

    // Vérifie que localStorage est vidé
    cy.window().its('session').should('not.exist');

    cy.url().should('not.include', '/sessions');
  });

  it('should show login/register if not logged', () => {

    cy.visit('', {
      onBeforeLoad(win) {
        win.CypressLoggedIn = false;
        win.isAdmin = false;
      }
    });

    // Supprime session
    cy.clearLocalStorage();
    cy.reload();

    cy.contains('Login').should('exist');
    cy.contains('Register').should('exist');
    cy.contains('Sessions').should('not.exist');
    cy.contains('Account').should('not.exist');
  });

});
