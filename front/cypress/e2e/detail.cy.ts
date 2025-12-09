describe('Session Detail Page', () => {

  const teacherMock = {
    id: 11,
    firstName: 'John',
    lastName: 'Doe',
    createdAt: '2025-01-01T10:00:00',
    updatedAt: '2025-01-01T10:00:00'
  };

  const sessionNotParticipated = {
    id: 100,
    name: 'Morning Yoga',
    description: 'A relaxing session',
    date: '2025-01-15T00:00:00.000+00:00',
    teacher_id: 11,
    users: [0], // non-participé
    createdAt: '2025-01-01T10:00:26',
    updatedAt: '2025-01-01T10:00:26'
  };

  const sessionParticipated = {
    ...sessionNotParticipated,
    users: [1] // déjà participé
  };

  beforeEach(() => {
    // Mock récupération teacher
    cy.intercept('GET', `api/teacher/${teacherMock.id}`, teacherMock).as('getTeacher');
  });

  it('should display session and teacher info', () => {
    // Mock récupération session
    cy.intercept('GET', `api/session/${sessionNotParticipated.id}`, sessionNotParticipated).as('getSession');
    // Mock participation
    cy.intercept('POST', `api/session/${sessionNotParticipated.id}/participate/1`, {}).as('participate');

    cy.visit(`/sessions/detail/${sessionNotParticipated.id}`, {
      onBeforeLoad(win) {
        win.CypressLoggedIn = true;
        win.isAdmin = false;
      }
    });

    cy.wait('@getSession');
    cy.wait('@getTeacher');

    cy.contains(sessionNotParticipated.name).should('exist');
    cy.contains(sessionNotParticipated.description).should('exist');
    cy.contains('John DOE').should('exist');
    cy.contains('1 attendees').should('exist');
    cy.get('button').contains('Participate').should('exist');
    cy.get('button').contains('Participate').click();
    cy.wait('@participate');
  });

  it('should participate/unparticipate in session', () => {
    cy.intercept('GET', `api/session/100`, sessionParticipated).as('getSession');
    cy.intercept('DELETE', `api/session/100/participate/1`, {}).as('unParticipate');
    cy.visit(`/sessions/detail/${sessionNotParticipated.id}`, {
      onBeforeLoad(win) {
        win.CypressLoggedIn = true;
        win.isAdmin = false;
      }
    });

    cy.wait('@getSession');
    cy.wait('@getTeacher');

    // Supposons qu'il n'a pas encore participé
    cy.get('button').contains('Do not participate').should('exist');
    cy.get('button').contains('Do not participate').click();
    cy.wait('@unParticipate');

  });

  it('should delete session for admin', () => {
    cy.intercept('GET', `api/session/${sessionNotParticipated.id}`, sessionNotParticipated).as('getSession');
    // Mock delete
    cy.intercept('DELETE', `api/session/${sessionNotParticipated.id}`, {}).as('deleteSession');

    cy.visit(`/sessions/detail/${sessionNotParticipated.id}`, {
      onBeforeLoad(win) {
        win.CypressLoggedIn = true;
        win.isAdmin = true;
      }
    });

    cy.wait('@getSession');
    cy.wait('@getTeacher');

    cy.get('button').contains('Delete').click();
    cy.wait('@deleteSession');

    cy.url().should('include', '/sessions');
  });

  it('should navigate back', () => {
    cy.intercept('GET', `api/session/${sessionNotParticipated.id}`, sessionNotParticipated).as('getSession');
    cy.visit('/me');
    cy.visit(`/sessions/detail/${sessionNotParticipated.id}`, {
      onBeforeLoad(win) {
        win.CypressLoggedIn = true;
        win.isAdmin = true;
      }
    });
    cy.get('button').contains('arrow_back').click();
    cy.url().should('not.include', '/sessions');
  });

});
