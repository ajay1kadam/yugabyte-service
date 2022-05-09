
# List all databases
\l

# List all schemas
\dn

# Create Database
create DATABASE hackathon;

# Connect to database
\c hackathon;

# Display  users
SELECT rolname, rolsuper, rolcanlogin FROM pg_roles;

\du

# Create user 
CREATE USER user1 WITH PASSWORD 'user#1';

# Grant all permissions hackathon  
GRANT ALL ON DATABASE hackathon TO user1;

# List all tables
\d

