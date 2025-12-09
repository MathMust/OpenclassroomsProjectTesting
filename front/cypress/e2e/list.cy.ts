describe('Session List Page', () => {

  const sessionsMock = [
    {
      id: 100,
      name: 'Morning Yoga',
      description: 'A relaxing session',
      date: '2025-01-15T00:00:00.000+00:00',
      teacher_id: 11,
      users: [],
      createdAt: '2025-01-01T10:00:26',
      updatedAt: '2025-01-01T10:00:26'
    },
    {
      id: 101,
      name: 'Evening Yoga',
      description: 'Relax and unwind',
      date: '2025-01-20T00:00:00.000+00:00',
      teacher_id: 12,
      users: [],
      createdAt: '2025-01-01T10:05:00.000+00:00',
      updatedAt: '2025-01-01T10:05:00.000+00:00'
    }
  ];

  beforeEach(() => {
    // Interception de la liste des sessions
    cy.intercept('GET', 'api/session', sessionsMock).as('getSessions');
  });

  it('should display list of sessions', () => {
    cy.visit('/sessions', {
      onBeforeLoad(win) {
        win.CypressLoggedIn = true;
        win.isAdmin = true;
      }
    });

    cy.wait('@getSessions');

    // Les liens doivent apparaître
    cy.contains('Sessions').should('exist');
    cy.contains('Account').should('exist');
    cy.contains('Logout').should('exist');
    // Les liens login/register ne doivent pas apparaître
    cy.contains('Login').should('not.exist');
    cy.contains('Register').should('not.exist');

    // Vérifie que chaque session est affichée
    sessionsMock.forEach(session => {
      cy.contains(session.name).should('exist');
      cy.contains(new Date(session.date).toLocaleDateString('en-US', { month: 'long', day: 'numeric', year: 'numeric' })).should('exist');
      cy.contains(session.description).should('exist');
    });

    // Vérifie que l’admin voit le bouton "Create"
    cy.get('button').contains('Create').should('exist');

    // Vérifie que chaque session a un bouton Detail
    sessionsMock.forEach(session => {
      cy.contains('button', 'Detail').should('exist');
      cy.contains('button', 'Edit').should('exist');
    });
  });

  it('should hide admin buttons for non-admin user', () => {
    // Remplace la session par un utilisateur non-admin

    cy.visit('/sessions', {
      onBeforeLoad(win) {
        win.CypressLoggedIn = true;
        win.isAdmin = false;
      }
    });
    cy.wait('@getSessions');

    // Le bouton "Create" ne doit pas exister
    cy.get('button').contains('Create').should('not.exist');

    // Les boutons "Edit" ne doivent pas exister
    cy.get('button').contains('Edit').should('not.exist');
  });

});
