# App Instance ID for AWS and Shedlock Example

Example for [aws-app-instance-id](https://github.com/alaugks/aws-app-instance-id) and the article
[Run Spring Scheduler Tasks Only Once Across Multiple Instances With ShedLock](https://alaugks.dev/article/run-spring-scheduler-tasks-only-once-across-multiple-instances-with-shedlock.html).

The instance ID is used as `locked_by` of ShedLock. The lock is stored in a MariaDB database.

| Profile         | Instance service                         |
|-----------------|------------------------------------------|
| `dev` (default) | `MockInstanceService`                    |
| `prod`, `stage` | `FargateInstanceService` with a fallback |

## Table of Contents

- [Setup](#setup)
  - [Requirements](#requirements)
  - [Clone](#clone)
  - [Run](#run)
  - [Show the lock table](#show-the-lock-table)
- [Database Migration](#database-migration)
  - [Manual migration](#manual-migration)
- [Differences to the article](#differences-to-the-article)

## Setup

### Requirements

- Docker

### Clone

```bash
git clone https://github.com/alaugks/aws-app-instance-id-shedlock-example.git
cd aws-app-instance-id-shedlock-example
```

### Run

Starts MariaDB and two instances of the application, `app-instance-1` (default profile) and `app-instance-2` (prod profile):

```bash
docker compose up --build
```

Both instances trigger the task every 10 seconds, but only the instance that gets the lock runs it and logs `processPendingJobs runs`. With a single instance, the task would run like a plain `@Scheduled` task.

### Show the Lock Table

`locked_by` contains the instance ID of the instance that holds the lock.

```bash
docker compose exec mariadb-instance-id-shedlock mariadb -ushedlock -pshedlock shedlock -e "SELECT * FROM shedlock"
```

## Database Migration

[`LiquibaseConfig`](src/main/java/io/github/alaugks/LiquibaseConfig.java) runs the Liquibase migration automatically on startup of the application.

On `prod` and `stage`, I prefer that the application runs the migration, not the CI/CD process. Locally, I prefer to run the migration manually (see [Manual migration](#manual-migration)). In this example, no profile is set and the application also runs the migration locally, because this makes the demonstration easier. To run it only on `prod` and `stage`:

```java
@Bean
@Profile({"prod", "stage"})
public SpringLiquibase liquibase(DataSource dataSource) { ... }
```

### Manual Migration

Runs the migration with the Liquibase CLI in Docker, e.g. for a profile in which `LiquibaseConfig` is not active:

```bash
docker compose run --rm liquibase update
```

## Differences to the Article

- Cron `*/10 * * * * *` instead of `0 */5 * * * *`.
