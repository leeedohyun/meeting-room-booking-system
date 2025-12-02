INSERT INTO meeting_room (name, capacity, hourly_rate, created_at, updated_at) VALUES ('회의실 A', 10, 50000, CURRENT_TIMESTAMP(), CURRENT_TIMESTAMP());
INSERT INTO meeting_room (name, capacity, hourly_rate, created_at, updated_at) VALUES ('회의실 B', 5, 30000, CURRENT_TIMESTAMP(), CURRENT_TIMESTAMP());

INSERT INTO users (username, email, created_at, updated_at) VALUES ('user1', 'test@email.com', NOW(), NOW());
