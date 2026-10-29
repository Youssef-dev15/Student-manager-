# Student Manager

Plateforme full-stack de gestion académique et administrative : suivi des étudiants, absences, bulletins de notes, dossiers médicaux et traçabilité des actions.
Projet réalisé dans le cadre d'un stage chez Auspify Technologies.

## Fonctionnalités

### Manager (secrétaire / pédagogique)
- CRUD des étudiants avec archivage (pas de suppression physique)
- Recherche paginée avec filtres combinables (nom, niveau, année d'inscription, absences)
- Suivi des absences (date, justifiée ou non, motif) avec compteur automatique
- Gestion des bulletins de notes et dossiers médicaux (PDF)

### Admin (super-administrateur)
- Tous les droits du Manager
- Validation des comptes Manager (PENDING → ACTIVE)
- Blocage / déblocage des comptes Manager
- Consultation du journal d'audit (lecture seule, paginé)

### Sécurité (dernière étape du projet)
- Authentification JWT, rôles ADMIN / MANAGER (RBAC)
- Changement de mot de passe, réinitialisation par l'admin
- Journalisation des actions sensibles

---

## Stack technique

| Couche | Technologies |
| :--- | :--- |
| **Backend** | Java 17+, Spring Boot, Spring Data JPA, Hibernate, Lombok |
| **Base de données** | Postgress |
| **Frontend (prévu)** | Angular, design system Midnight Blue & Cyan, mode Dark/Light, responsive |
| **Outils** | Maven, Docker |

---

## Architecture du dépôt

```text
student-manager/
├── backend/                  # API Spring Boot
├── frontend/                 # Application Angular
├── docker-compose.yml        # postegres + backend + frontend
├── .env.example              
├── .gitignore
└── README.md