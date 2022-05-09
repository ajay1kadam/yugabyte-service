
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
CREATE USER user4 WITH PASSWORD 'user#4';

# Grant all permissions hackathon  
GRANT ALL ON DATABASE hackathon TO user4;

# List all tables
\d

# Create & grant access to sequence 
CREATE SEQUENCE hibernate_sequence START 1;
GRANT USAGE, SELECT ON SEQUENCE hibernate_sequence TO user4;