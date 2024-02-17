-- Tournament Type
INSERT IGNORE INTO tournament_db.tournament_type(tournament_type_id, tournament_type, record_created_date, record_created_by, is_active)
VALUES(1, 'cricket', CURRENT_DATE(), UUID(), true);

-- Tournaments
INSERT IGNORE INTO tournament_db.tournament(tournament_id, tournament_type_id, tournament_name, tournament_description, tournament_year, maximum_overs_per_match, record_created_date, record_created_by, is_active)
VALUES(101, 1, 'Cricket World Cup', 'Cricket World Cup', 2023, 50, CURRENT_DATE(), UUID(), true);

INSERT IGNORE INTO tournament_db.tournament(tournament_id, tournament_type_id, tournament_name, tournament_description, tournament_year, maximum_overs_per_match, record_created_date, record_created_by, is_active)
VALUES(102, 1, 'IPL', 'Indian Premier League', 2019, 20, CURRENT_DATE(), UUID(), true);

-- Teams

-- Team by franchise
INSERT IGNORE INTO tournament_db.team(team_id, team_name, team_owner, record_created_date, record_created_by, is_active)
VALUES (1111, 'Chennai Super Kings', 'Chennai Super Kings', CURRENT_DATE(), UUID(), true);

INSERT IGNORE INTO tournament_db.team(team_id, team_name, team_owner, record_created_date, record_created_by, is_active)
VALUES (1112, 'Mumbai Indians', 'Mumbai Indians', CURRENT_DATE(), UUID(), true);

INSERT IGNORE INTO tournament_db.team(team_id, team_name, team_owner, record_created_date, record_created_by, is_active)
VALUES (1113, 'Royal Challengers Bangalore', 'Royal Challengers Bangalore', CURRENT_DATE(), UUID(), true);

INSERT IGNORE INTO tournament_db.team(team_id, team_name, team_owner, record_created_date, record_created_by, is_active)
VALUES (1114, 'Kolkata Knight Riders', 'Kolkata Knight Riders', CURRENT_DATE(), UUID(), true);

INSERT IGNORE INTO tournament_db.team(team_id, team_name, team_owner, record_created_date, record_created_by, is_active)
VALUES (1115, 'Sunrisers Hyderabad', 'Sunrisers Hyderabad', CURRENT_DATE(), UUID(), true);

INSERT IGNORE INTO tournament_db.team(team_id, team_name, team_owner, record_created_date, record_created_by, is_active)
VALUES (1116, 'Rajasthan Royals', 'Rajasthan Royals', CURRENT_DATE(), UUID(), true);

INSERT IGNORE INTO tournament_db.team(team_id, team_name, team_owner, record_created_date, record_created_by, is_active)
VALUES (1117, 'Delhi Capitals', 'Delhi Capitals', CURRENT_DATE(), UUID(), true);

INSERT IGNORE INTO tournament_db.team(team_id, team_name, team_owner, record_created_date, record_created_by, is_active)
VALUES (1118, 'Punjab Kings', 'Punjab Kings', CURRENT_DATE(), UUID(), true);

INSERT IGNORE INTO tournament_db.team(team_id, team_name, team_owner, record_created_date, record_created_by, is_active)
VALUES (1119, 'Lucknow Super Giants', 'Lucknow Super Giants', CURRENT_DATE(), UUID(), true);

INSERT IGNORE INTO tournament_db.team(team_id, team_name, team_owner, record_created_date, record_created_by, is_active)
VALUES (1120, 'Gujarat Titans', 'Gujarat Titans', CURRENT_DATE(), UUID(), true);

-- Teams by country
INSERT IGNORE INTO tournament_db.team(team_id, team_name, team_owner, record_created_date, record_created_by, is_active)
VALUES (1101, 'India', 'Indian Cricket Team', CURRENT_DATE(), UUID(), true);

INSERT IGNORE INTO tournament_db.team(team_id, team_name, team_owner, record_created_date, record_created_by, is_active)
VALUES (1102, 'Australia', 'Australian Cricket Team', CURRENT_DATE(), UUID(), true);

INSERT IGNORE INTO tournament_db.team(team_id, team_name, team_owner, record_created_date, record_created_by, is_active)
VALUES (1103, 'England', 'England Cricket Team', CURRENT_DATE(), UUID(), true);

INSERT IGNORE INTO tournament_db.team(team_id, team_name, team_owner, record_created_date, record_created_by, is_active)
VALUES (1104, 'South Africa', 'South African Cricket Team', CURRENT_DATE(), UUID(), true);

INSERT IGNORE INTO tournament_db.team(team_id, team_name, team_owner, record_created_date, record_created_by, is_active)
VALUES (1105, 'New Zealand', 'New Zealand Cricket Team', CURRENT_DATE(), UUID(), true);

INSERT IGNORE INTO tournament_db.team(team_id, team_name, team_owner, record_created_date, record_created_by, is_active)
VALUES (1106, 'Pakistan', 'Pakistan Cricket Team', CURRENT_DATE(), UUID(), true);

INSERT IGNORE INTO tournament_db.team(team_id, team_name, team_owner, record_created_date, record_created_by, is_active)
VALUES (1107, 'Sri Lanka', 'Sri Lankan Cricket Team', CURRENT_DATE(), UUID(), true);

INSERT IGNORE INTO tournament_db.team(team_id, team_name, team_owner, record_created_date, record_created_by, is_active)
VALUES (1108, 'Afghanistan', 'Afghanistan Cricket Team', CURRENT_DATE(), UUID(), true);

INSERT IGNORE INTO tournament_db.team(team_id, team_name, team_owner, record_created_date, record_created_by, is_active)
VALUES (1109, 'Bangladesh', 'Bangladesh Cricket Team', CURRENT_DATE(), UUID(), true);

INSERT IGNORE INTO tournament_db.team(team_id, team_name, team_owner, record_created_date, record_created_by, is_active)
VALUES (1110, 'Netherlands', 'Netherlands Cricket Team', CURRENT_DATE(), UUID(), true);

-- Team Stats
INSERT IGNORE INTO tournament_db.team_stats(team_stats_id, team_id, tournament_id, total_runs_scored, total_overs_played, total_runs_conceded, total_overs_bowled, record_created_date, record_created_by, is_active)
VALUES
  (301, 1101, 101, 0, 0, 0, 0, CURRENT_DATE(), UUID(), true),
  (302, 1102, 101, 0, 0, 0, 0, CURRENT_DATE(), UUID(), true),
  (303, 1103, 101, 0, 0, 0, 0, CURRENT_DATE(), UUID(), true),
  (304, 1104, 101, 0, 0, 0, 0, CURRENT_DATE(), UUID(), true),
  (305, 1105, 101, 0, 0, 0, 0, CURRENT_DATE(), UUID(), true),
  (306, 1106, 101, 0, 0, 0, 0, CURRENT_DATE(), UUID(), true),
  (307, 1107, 101, 0, 0, 0, 0, CURRENT_DATE(), UUID(), true),
  (308, 1108, 101, 0, 0, 0, 0, CURRENT_DATE(), UUID(), true),
  (309, 1109, 101, 0, 0, 0, 0, CURRENT_DATE(), UUID(), true),
  (310, 1110, 101, 0, 0, 0, 0, CURRENT_DATE(), UUID(), true);

-- PointsTable
INSERT IGNORE INTO tournament_db.points_table(points_table_id, tournament_id, team_id, played, won, lost, tied, no_result, points, net_match_rate, record_created_date, record_created_by, is_active)
VALUES
  (401, 101, 1101, 0, 0, 0, 0, 0, 0, 0, CURRENT_DATE(), UUID(), true),
  (402, 101, 1102, 0, 0, 0, 0, 0, 0, 0, CURRENT_DATE(), UUID(), true),
  (403, 101, 1103, 0, 0, 0, 0, 0, 0, 0, CURRENT_DATE(), UUID(), true),
  (404, 101, 1104, 0, 0, 0, 0, 0, 0, 0, CURRENT_DATE(), UUID(), true),
  (405, 101, 1105, 0, 0, 0, 0, 0, 0, 0, CURRENT_DATE(), UUID(), true),
  (406, 101, 1106, 0, 0, 0, 0, 0, 0, 0, CURRENT_DATE(), UUID(), true),
  (407, 101, 1107, 0, 0, 0, 0, 0, 0, 0, CURRENT_DATE(), UUID(), true),
  (408, 101, 1108, 0, 0, 0, 0, 0, 0, 0, CURRENT_DATE(), UUID(), true),
  (409, 101, 1109, 0, 0, 0, 0, 0, 0, 0, CURRENT_DATE(), UUID(), true),
  (410, 101, 1110, 0, 0, 0, 0, 0, 0, 0, CURRENT_DATE(), UUID(), true),

  (411, 102, 1111, 0, 0, 0, 0, 0, 0, 0, CURRENT_DATE(), UUID(), true),
  (412, 102, 1112, 0, 0, 0, 0, 0, 0, 0, CURRENT_DATE(), UUID(), true),
  (413, 102, 1113, 0, 0, 0, 0, 0, 0, 0, CURRENT_DATE(), UUID(), true),
  (414, 102, 1114, 0, 0, 0, 0, 0, 0, 0, CURRENT_DATE(), UUID(), true),
  (415, 102, 1115, 0, 0, 0, 0, 0, 0, 0, CURRENT_DATE(), UUID(), true),
  (416, 102, 1116, 0, 0, 0, 0, 0, 0, 0, CURRENT_DATE(), UUID(), true),
  (417, 102, 1117, 0, 0, 0, 0, 0, 0, 0, CURRENT_DATE(), UUID(), true),
  (418, 102, 1118, 0, 0, 0, 0, 0, 0, 0, CURRENT_DATE(), UUID(), true),
  (419, 102, 1119, 0, 0, 0, 0, 0, 0, 0, CURRENT_DATE(), UUID(), true),
  (420, 102, 1120, 0, 0, 0, 0, 0, 0, 0, CURRENT_DATE(), UUID(), true);


