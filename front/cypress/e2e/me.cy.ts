describe('MeComponent - E2E avec mock backend et session', () => {

    // 🔹 Fonction utilitaire pour mocker l’API GET user
    const mockGetUser = (isAdmin?: boolean) => {
        cy.intercept('GET', '/api/user/1', {
            statusCode: 200,
            body: {
                id: 1,
                username: 'Test',
                firstName: 'Test',
                lastName: 'User',
                email: 'test@example.com',
                admin: isAdmin !== undefined ? isAdmin : false,
                createdAt: new Date(),
                updatedAt: new Date()
            }
        }).as('getUser');
    };

    const mockDeleteUser = () => {
        cy.intercept('DELETE', '/api/user/1', {
            statusCode: 200,
            body: {}
        }).as('deleteUser');
    };

    context('Non-admin user', () => {
        beforeEach(() => {
            mockGetUser(); // par défaut non-admin
            mockDeleteUser();
            cy.visit('/sessions');
            cy.visit('/me', {
                onBeforeLoad(win) {
                    // Définit le flag avant que Angular ne monte le service
                    win.CypressLoggedIn = true;
                }
            });
        });

        it('should display user info', () => {
            cy.wait('@getUser');
            cy.contains('Name: Test USER');
            cy.contains('Email: test@example.com');
            cy.get('button').contains('Detail').should('exist');
        });

        it('should allow deleting user', () => {
            cy.wait('@getUser');
            cy.get('button').contains('Detail').click();
            cy.wait('@deleteUser');
            cy.contains('Your account has been deleted !').should('exist');
        });


        it('should navigate back', () => {
            cy.get('button').contains('arrow_back').click();
            cy.url().should('not.include', '/me');
        });
    });

    context('Admin user', () => {
        beforeEach(() => {
            mockGetUser(true); // admin
            cy.visit('/me', {
                onBeforeLoad(win) {
                    // Définit le flag avant que Angular ne monte le service
                    win.CypressLoggedIn = true;
                }
            });
        });

        it('should display admin info', () => {
            cy.wait('@getUser');
            cy.contains('Name: Test USER');
            cy.contains('Email: test@example.com');
            cy.get('button').contains('Detail').should('not.exist');
            cy.contains('You are admin').should('exist');
        });
    });

});
