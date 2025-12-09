describe('Session Form - Create', () => {

  beforeEach(() => {
    // Mock des enseignants
    cy.intercept('GET', 'api/teacher', [
      {
        id: 11, lastName: 'Doe', firstName: 'John', createdAt: '2025-01-01T10:00:00', updatedAt: '2025-01-01T10:00:00'
      },
      { id: 12, lastName: 'Bell', firstName: 'Anna', createdAt: '2025-01-01T10:00:00', updatedAt: '2025-01-01T10:00:00' }
    ]).as('getTeachers');

    // Mock création session
    cy.intercept('POST', 'api/session', {
      id: 100,
      name: 'Morning Yoga',
      description: 'Description test...',
      date: '2025-01-15T00:00:00.000+00:00',
      teacher_id: 11,
      users: [],
      createdAt: '2025-01-01T10:00:26',
      updatedAt: '2025-01-01T10:00:26'
    }).as('createSession');

    // Mock création session
    cy.intercept('Get', 'api/session', {
      id: 100,
      name: 'Morning Yoga',
      description: 'Description test...',
      date: '2025-01-15T00:00:00.000+00:00',
      teacher_id: 11,
      users: [],
      createdAt: '2025-01-01T10:00:26',
      updatedAt: '2025-01-01T10:00:26'
    }).as('getSession');
  });

  it('should create a session', () => {
    cy.visit('/sessions/create', {
      onBeforeLoad(win) {
        // Définit le flag avant que Angular ne monte le service
        win.CypressLoggedIn = true;
        win.isAdmin = true;
      }
    });

    cy.wait('@getTeachers');

    cy.get('input[formControlName="name"]').type('Morning Yoga');
    cy.get('input[formControlName="date"]').type('2025-01-15');

    cy.get('mat-select[formControlName="teacher_id"]').click();
    cy.contains('John Doe').click();

    cy.get('textarea[formControlName="description"]').type('Description test...');

    cy.get('button[type="submit"]').should('not.be.disabled').click();

    cy.wait('@createSession');

    cy.url().should('include', '/sessions');
  });

  it('should not admin', () => {
    cy.visit('/sessions/create', {
      onBeforeLoad(win) {
        // Définit le flag avant que Angular ne monte le service
        win.CypressLoggedIn = true;
        win.isAdmin = false;
      }
    });

    cy.url().should('include', '/sessions');
  });
});

describe('Session Form - Update', () => {

  const existingSession = {
    id: 100,
    name: 'Morning Yoga',
    description: 'Description test...',
    date: '2025-01-15T00:00:00.000+00:00',
    teacher_id: 11,
    users: [],
    createdAt: '2025-01-01T10:00:26',
    updatedAt: '2025-01-01T10:00:26'
  };

  beforeEach(() => {
    // Mock des enseignants
    cy.intercept('GET', 'api/teacher', [
      { id: 11, lastName: 'Doe', firstName: 'John', createdAt: '2025-01-01T10:00:00', updatedAt: '2025-01-01T10:00:00' },
      { id: 12, lastName: 'Bell', firstName: 'Anna', createdAt: '2025-01-01T10:00:00', updatedAt: '2025-01-01T10:00:00' }
    ]).as('getTeachers');

    // Mock session existante
    cy.intercept('GET', `api/session/${existingSession.id}`, existingSession).as('getSession');

    // Mock mise à jour
    cy.intercept('PUT', `api/session/${existingSession.id}`, (req) => {
      req.reply({
        ...existingSession,
        name: req.body.name,
        description: req.body.description,
        date: req.body.date,
        teacher_id: req.body.teacher_id,
        updatedAt: new Date().toISOString()
      });
    }).as('updateSession');
  });

  it('should update a session', () => {
    cy.visit(`/sessions/update/${existingSession.id}`, {
      onBeforeLoad(win) {
        win.CypressLoggedIn = true;
        win.isAdmin = true;
      }
    });

    // Attend que les enseignants et session soient chargés
    cy.wait('@getTeachers');
    cy.wait('@getSession');

    // Vérifie que le formulaire est pré-rempli
    cy.get('input[formControlName="name"]').should('have.value', existingSession.name);
    cy.get('textarea[formControlName="description"]').should('have.value', existingSession.description);

    // Modifie le formulaire
    cy.get('input[formControlName="name"]').clear().type('Evening Yoga');
    cy.get('input[formControlName="date"]').clear().type('2025-02-01');

    cy.get('mat-select[formControlName="teacher_id"]').click();
    cy.contains('Anna Bell').click();

    cy.get('textarea[formControlName="description"]').clear().type('Updated description');

    // Soumet
    cy.get('button[type="submit"]').should('not.be.disabled').click();

    // Vérifie l’appel API PUT
    cy.wait('@updateSession');

    // Vérifie redirection
    cy.url().should('include', '/sessions');
  });
});
