-- Tournament Type
INSERT IGNORE INTO tournament_db.tournament_type(tournament_type_id, tournament_type, record_created_date, record_created_by, is_active)
VALUES(1, 'cricket', CURRENT_DATE(), "admin", true);

-- Tournaments
INSERT IGNORE INTO tournament_db.tournament(tournament_id, tournament_type_id, tournament_name, tournament_description, tournament_year, maximum_overs_per_match, record_created_date, record_created_by, is_active)
VALUES(101, 1, 'Cricket World Cup 2023', 'Cricket World Cup', 2023, 50, CURRENT_DATE(), "admin", true);

INSERT IGNORE INTO tournament_db.tournament(tournament_id, tournament_type_id, tournament_name, tournament_description, tournament_year, maximum_overs_per_match, record_created_date, record_created_by, is_active)
VALUES(102, 1, 'IPL 2023', 'Indian Premier League', 2023, 20, CURRENT_DATE(), "admin", true);

INSERT IGNORE INTO tournament_db.tournament(tournament_id, tournament_type_id, tournament_name, tournament_description, tournament_year, maximum_overs_per_match, record_created_date, record_created_by, is_active)
VALUES(103, 1, 'Cricket World Cup 2024', 'Cricket World Cup', 2024, 50, CURRENT_DATE(), "admin", true);

INSERT IGNORE INTO tournament_db.tournament(tournament_id, tournament_type_id, tournament_name, tournament_description, tournament_year, maximum_overs_per_match, record_created_date, record_created_by, is_active)
VALUES(104, 1, 'IPL 2024', 'Indian Premier League', 2024, 20, CURRENT_DATE(), "admin", true);


-- Teams

-- Team by franchise
INSERT IGNORE INTO tournament_db.team(team_id, team_name, team_owner, record_created_date, record_created_by, is_active)
VALUES (1111, 'Chennai Super Kings', 'Chennai Super Kings', CURRENT_DATE(), "admin", true);

INSERT IGNORE INTO tournament_db.team(team_id, team_name, team_owner, record_created_date, record_created_by, is_active)
VALUES (1112, 'Mumbai Indians', 'Mumbai Indians', CURRENT_DATE(), "admin", true);

INSERT IGNORE INTO tournament_db.team(team_id, team_name, team_owner, record_created_date, record_created_by, is_active)
VALUES (1113, 'Royal Challengers Bangalore', 'Royal Challengers Bangalore', CURRENT_DATE(), "admin", true);

INSERT IGNORE INTO tournament_db.team(team_id, team_name, team_owner, record_created_date, record_created_by, is_active)
VALUES (1114, 'Kolkata Knight Riders', 'Kolkata Knight Riders', CURRENT_DATE(), "admin", true);

INSERT IGNORE INTO tournament_db.team(team_id, team_name, team_owner, record_created_date, record_created_by, is_active)
VALUES (1115, 'Sunrisers Hyderabad', 'Sunrisers Hyderabad', CURRENT_DATE(), "admin", true);

INSERT IGNORE INTO tournament_db.team(team_id, team_name, team_owner, record_created_date, record_created_by, is_active)
VALUES (1116, 'Rajasthan Royals', 'Rajasthan Royals', CURRENT_DATE(), "admin", true);

INSERT IGNORE INTO tournament_db.team(team_id, team_name, team_owner, record_created_date, record_created_by, is_active)
VALUES (1117, 'Delhi Capitals', 'Delhi Capitals', CURRENT_DATE(), "admin", true);

INSERT IGNORE INTO tournament_db.team(team_id, team_name, team_owner, record_created_date, record_created_by, is_active)
VALUES (1118, 'Punjab Kings', 'Punjab Kings', CURRENT_DATE(), "admin", true);

INSERT IGNORE INTO tournament_db.team(team_id, team_name, team_owner, record_created_date, record_created_by, is_active)
VALUES (1119, 'Lucknow Super Giants', 'Lucknow Super Giants', CURRENT_DATE(), "admin", true);

INSERT IGNORE INTO tournament_db.team(team_id, team_name, team_owner, record_created_date, record_created_by, is_active)
VALUES (1120, 'Gujarat Titans', 'Gujarat Titans', CURRENT_DATE(), "admin", true);

-- Teams by country
INSERT IGNORE INTO tournament_db.team(team_id, team_name, team_owner, record_created_date, record_created_by, is_active)
VALUES (1101, 'India', 'Indian Cricket Team', CURRENT_DATE(), "admin", true);

INSERT IGNORE INTO tournament_db.team(team_id, team_name, team_owner, record_created_date, record_created_by, is_active)
VALUES (1102, 'Australia', 'Australian Cricket Team', CURRENT_DATE(), "admin", true);

INSERT IGNORE INTO tournament_db.team(team_id, team_name, team_owner, record_created_date, record_created_by, is_active)
VALUES (1103, 'England', 'England Cricket Team', CURRENT_DATE(), "admin", true);

INSERT IGNORE INTO tournament_db.team(team_id, team_name, team_owner, record_created_date, record_created_by, is_active)
VALUES (1104, 'South Africa', 'South African Cricket Team', CURRENT_DATE(), "admin", true);

INSERT IGNORE INTO tournament_db.team(team_id, team_name, team_owner, record_created_date, record_created_by, is_active)
VALUES (1105, 'New Zealand', 'New Zealand Cricket Team', CURRENT_DATE(), "admin", true);

INSERT IGNORE INTO tournament_db.team(team_id, team_name, team_owner, record_created_date, record_created_by, is_active)
VALUES (1106, 'Pakistan', 'Pakistan Cricket Team', CURRENT_DATE(), "admin", true);

INSERT IGNORE INTO tournament_db.team(team_id, team_name, team_owner, record_created_date, record_created_by, is_active)
VALUES (1107, 'Sri Lanka', 'Sri Lankan Cricket Team', CURRENT_DATE(), "admin", true);

INSERT IGNORE INTO tournament_db.team(team_id, team_name, team_owner, record_created_date, record_created_by, is_active)
VALUES (1108, 'Afghanistan', 'Afghanistan Cricket Team', CURRENT_DATE(), "admin", true);

INSERT IGNORE INTO tournament_db.team(team_id, team_name, team_owner, record_created_date, record_created_by, is_active)
VALUES (1109, 'Bangladesh', 'Bangladesh Cricket Team', CURRENT_DATE(), "admin", true);

INSERT IGNORE INTO tournament_db.team(team_id, team_name, team_owner, record_created_date, record_created_by, is_active)
VALUES (1110, 'Netherlands', 'Netherlands Cricket Team', CURRENT_DATE(), "admin", true);

-- Team To Tournament Mapping
INSERT IGNORE INTO tournament_db.team_to_tournament_mapping(mapping_id, team_id, tournament_id, record_created_date, record_created_by, is_active)
VALUES
  (501, 1111, 102, CURRENT_DATE(), "admin", true),
  (502, 1112, 102, CURRENT_DATE(), "admin", true),
  (503, 1113, 102, CURRENT_DATE(), "admin", true),
  (504, 1114, 102, CURRENT_DATE(), "admin", true),
  (505, 1115, 102, CURRENT_DATE(), "admin", true),
  (506, 1116, 102, CURRENT_DATE(), "admin", true),
  (507, 1117, 102, CURRENT_DATE(), "admin", true),
  (508, 1118, 102, CURRENT_DATE(), "admin", true),
  (509, 1119, 102, CURRENT_DATE(), "admin", true),
  (510, 1120, 102, CURRENT_DATE(), "admin", true),
  (511, 1101, 101, CURRENT_DATE(), "admin", true),
  (512, 1102, 101, CURRENT_DATE(), "admin", true),
  (513, 1103, 101, CURRENT_DATE(), "admin", true),
  (514, 1104, 101, CURRENT_DATE(), "admin", true),
  (515, 1105, 101, CURRENT_DATE(), "admin", true),
  (516, 1106, 101, CURRENT_DATE(), "admin", true),
  (517, 1107, 101, CURRENT_DATE(), "admin", true),
  (518, 1108, 101, CURRENT_DATE(), "admin", true),
  (519, 1109, 101, CURRENT_DATE(), "admin", true),
  (520, 1110, 101, CURRENT_DATE(), "admin", true);

INSERT IGNORE INTO tournament_db.team_to_tournament_mapping(mapping_id, team_id, tournament_id, record_created_date, record_created_by, is_active)
VALUES
  (521, 1111, 104, CURRENT_DATE(), "admin", true),
  (522, 1112, 104, CURRENT_DATE(), "admin", true),
  (523, 1113, 104, CURRENT_DATE(), "admin", true),
  (524, 1114, 104, CURRENT_DATE(), "admin", true),
  (525, 1115, 104, CURRENT_DATE(), "admin", true),
  (526, 1116, 104, CURRENT_DATE(), "admin", true),
  (527, 1117, 104, CURRENT_DATE(), "admin", true),
  (528, 1118, 104, CURRENT_DATE(), "admin", true),
  (529, 1119, 104, CURRENT_DATE(), "admin", true),
  (530, 1120, 104, CURRENT_DATE(), "admin", true),
  (531, 1101, 103, CURRENT_DATE(), "admin", true),
  (532, 1102, 103, CURRENT_DATE(), "admin", true),
  (533, 1103, 103, CURRENT_DATE(), "admin", true),
  (534, 1104, 103, CURRENT_DATE(), "admin", true),
  (535, 1105, 103, CURRENT_DATE(), "admin", true),
  (536, 1106, 103, CURRENT_DATE(), "admin", true),
  (537, 1107, 103, CURRENT_DATE(), "admin", true),
  (538, 1108, 103, CURRENT_DATE(), "admin", true),
  (539, 1109, 103, CURRENT_DATE(), "admin", true),
  (540, 1110, 103, CURRENT_DATE(), "admin", true);


-- Team Stats
INSERT IGNORE INTO tournament_db.team_stats(team_stats_id, team_id, tournament_id, total_runs_scored, total_overs_played, total_runs_conceded, total_overs_bowled, record_created_date, record_created_by, is_active)
VALUES
  (301, 1101, 101, 0, 0, 0, 0, CURRENT_DATE(), "admin", true),
  (302, 1102, 101, 0, 0, 0, 0, CURRENT_DATE(), "admin", true),
  (303, 1103, 101, 0, 0, 0, 0, CURRENT_DATE(), "admin", true),
  (304, 1104, 101, 0, 0, 0, 0, CURRENT_DATE(), "admin", true),
  (305, 1105, 101, 0, 0, 0, 0, CURRENT_DATE(), "admin", true),
  (306, 1106, 101, 0, 0, 0, 0, CURRENT_DATE(), "admin", true),
  (307, 1107, 101, 0, 0, 0, 0, CURRENT_DATE(), "admin", true),
  (308, 1108, 101, 0, 0, 0, 0, CURRENT_DATE(), "admin", true),
  (309, 1109, 101, 0, 0, 0, 0, CURRENT_DATE(), "admin", true),
  (310, 1110, 101, 0, 0, 0, 0, CURRENT_DATE(), "admin", true);

INSERT IGNORE INTO tournament_db.team_stats(team_stats_id, team_id, tournament_id, total_runs_scored, total_overs_played, total_runs_conceded, total_overs_bowled, record_created_date, record_created_by, is_active)
VALUES
  (321, 1101, 103, 0, 0, 0, 0, CURRENT_DATE(), "admin", true),
  (322, 1102, 103, 0, 0, 0, 0, CURRENT_DATE(), "admin", true),
  (323, 1103, 103, 0, 0, 0, 0, CURRENT_DATE(), "admin", true),
  (324, 1104, 103, 0, 0, 0, 0, CURRENT_DATE(), "admin", true),
  (325, 1105, 103, 0, 0, 0, 0, CURRENT_DATE(), "admin", true),
  (326, 1106, 103, 0, 0, 0, 0, CURRENT_DATE(), "admin", true),
  (327, 1107, 103, 0, 0, 0, 0, CURRENT_DATE(), "admin", true),
  (328, 1108, 103, 0, 0, 0, 0, CURRENT_DATE(), "admin", true),
  (329, 1109, 103, 0, 0, 0, 0, CURRENT_DATE(), "admin", true),
  (330, 1110, 103, 0, 0, 0, 0, CURRENT_DATE(), "admin", true);

INSERT IGNORE INTO tournament_db.team_stats(team_stats_id, team_id, tournament_id, total_runs_scored, total_overs_played, total_runs_conceded, total_overs_bowled, record_created_date, record_created_by, is_active)
VALUES
  (311, 1111, 102, 0, 0, 0, 0, CURRENT_DATE(), "admin", true),
  (312, 1112, 102, 0, 0, 0, 0, CURRENT_DATE(), "admin", true),
  (313, 1113, 102, 0, 0, 0, 0, CURRENT_DATE(), "admin", true),
  (314, 1114, 102, 0, 0, 0, 0, CURRENT_DATE(), "admin", true),
  (315, 1115, 102, 0, 0, 0, 0, CURRENT_DATE(), "admin", true),
  (316, 1116, 102, 0, 0, 0, 0, CURRENT_DATE(), "admin", true),
  (317, 1117, 102, 0, 0, 0, 0, CURRENT_DATE(), "admin", true),
  (318, 1118, 102, 0, 0, 0, 0, CURRENT_DATE(), "admin", true),
  (319, 1119, 102, 0, 0, 0, 0, CURRENT_DATE(), "admin", true),
  (320, 1120, 102, 0, 0, 0, 0, CURRENT_DATE(), "admin", true);

INSERT IGNORE INTO tournament_db.team_stats(team_stats_id, team_id, tournament_id, total_runs_scored, total_overs_played, total_runs_conceded, total_overs_bowled, record_created_date, record_created_by, is_active)
VALUES
  (331, 1111, 104, 0, 0, 0, 0, CURRENT_DATE(), "admin", true),
  (332, 1112, 104, 0, 0, 0, 0, CURRENT_DATE(), "admin", true),
  (333, 1113, 104, 0, 0, 0, 0, CURRENT_DATE(), "admin", true),
  (334, 1114, 104, 0, 0, 0, 0, CURRENT_DATE(), "admin", true),
  (335, 1115, 104, 0, 0, 0, 0, CURRENT_DATE(), "admin", true),
  (336, 1116, 104, 0, 0, 0, 0, CURRENT_DATE(), "admin", true),
  (337, 1117, 104, 0, 0, 0, 0, CURRENT_DATE(), "admin", true),
  (338, 1118, 104, 0, 0, 0, 0, CURRENT_DATE(), "admin", true),
  (339, 1119, 104, 0, 0, 0, 0, CURRENT_DATE(), "admin", true),
  (340, 1120, 104, 0, 0, 0, 0, CURRENT_DATE(), "admin", true);

-- PointsTable
INSERT IGNORE INTO tournament_db.points_table(points_table_id, tournament_id, team_id, played, won, lost, tied, no_result, points, net_match_rate, record_created_date, record_created_by, is_active)
VALUES
  (401, 101, 1101, 0, 0, 0, 0, 0, 0, 0, CURRENT_DATE(), "admin", true),
  (402, 101, 1102, 0, 0, 0, 0, 0, 0, 0, CURRENT_DATE(), "admin", true),
  (403, 101, 1103, 0, 0, 0, 0, 0, 0, 0, CURRENT_DATE(), "admin", true),
  (404, 101, 1104, 0, 0, 0, 0, 0, 0, 0, CURRENT_DATE(), "admin", true),
  (405, 101, 1105, 0, 0, 0, 0, 0, 0, 0, CURRENT_DATE(), "admin", true),
  (406, 101, 1106, 0, 0, 0, 0, 0, 0, 0, CURRENT_DATE(), "admin", true),
  (407, 101, 1107, 0, 0, 0, 0, 0, 0, 0, CURRENT_DATE(), "admin", true),
  (408, 101, 1108, 0, 0, 0, 0, 0, 0, 0, CURRENT_DATE(), "admin", true),
  (409, 101, 1109, 0, 0, 0, 0, 0, 0, 0, CURRENT_DATE(), "admin", true),
  (410, 101, 1110, 0, 0, 0, 0, 0, 0, 0, CURRENT_DATE(), "admin", true),

  (411, 102, 1111, 0, 0, 0, 0, 0, 0, 0, CURRENT_DATE(), "admin", true),
  (412, 102, 1112, 0, 0, 0, 0, 0, 0, 0, CURRENT_DATE(), "admin", true),
  (413, 102, 1113, 0, 0, 0, 0, 0, 0, 0, CURRENT_DATE(), "admin", true),
  (414, 102, 1114, 0, 0, 0, 0, 0, 0, 0, CURRENT_DATE(), "admin", true),
  (415, 102, 1115, 0, 0, 0, 0, 0, 0, 0, CURRENT_DATE(), "admin", true),
  (416, 102, 1116, 0, 0, 0, 0, 0, 0, 0, CURRENT_DATE(), "admin", true),
  (417, 102, 1117, 0, 0, 0, 0, 0, 0, 0, CURRENT_DATE(), "admin", true),
  (418, 102, 1118, 0, 0, 0, 0, 0, 0, 0, CURRENT_DATE(), "admin", true),
  (419, 102, 1119, 0, 0, 0, 0, 0, 0, 0, CURRENT_DATE(), "admin", true),
  (420, 102, 1120, 0, 0, 0, 0, 0, 0, 0, CURRENT_DATE(), "admin", true);


INSERT IGNORE INTO tournament_db.points_table(points_table_id, tournament_id, team_id, played, won, lost, tied, no_result, points, net_match_rate, record_created_date, record_created_by, is_active)
VALUES
  (421, 103, 1101, 0, 0, 0, 0, 0, 0, 0, CURRENT_DATE(), "admin", true),
  (422, 103, 1102, 0, 0, 0, 0, 0, 0, 0, CURRENT_DATE(), "admin", true),
  (423, 103, 1103, 0, 0, 0, 0, 0, 0, 0, CURRENT_DATE(), "admin", true),
  (424, 103, 1104, 0, 0, 0, 0, 0, 0, 0, CURRENT_DATE(), "admin", true),
  (425, 103, 1105, 0, 0, 0, 0, 0, 0, 0, CURRENT_DATE(), "admin", true),
  (426, 103, 1106, 0, 0, 0, 0, 0, 0, 0, CURRENT_DATE(), "admin", true),
  (427, 103, 1107, 0, 0, 0, 0, 0, 0, 0, CURRENT_DATE(), "admin", true),
  (428, 103, 1108, 0, 0, 0, 0, 0, 0, 0, CURRENT_DATE(), "admin", true),
  (429, 103, 1109, 0, 0, 0, 0, 0, 0, 0, CURRENT_DATE(), "admin", true),
  (420, 103, 1110, 0, 0, 0, 0, 0, 0, 0, CURRENT_DATE(), "admin", true),

  (431, 104, 1111, 0, 0, 0, 0, 0, 0, 0, CURRENT_DATE(), "admin", true),
  (432, 104, 1112, 0, 0, 0, 0, 0, 0, 0, CURRENT_DATE(), "admin", true),
  (433, 104, 1113, 0, 0, 0, 0, 0, 0, 0, CURRENT_DATE(), "admin", true),
  (434, 104, 1114, 0, 0, 0, 0, 0, 0, 0, CURRENT_DATE(), "admin", true),
  (435, 104, 1115, 0, 0, 0, 0, 0, 0, 0, CURRENT_DATE(), "admin", true),
  (436, 104, 1116, 0, 0, 0, 0, 0, 0, 0, CURRENT_DATE(), "admin", true),
  (437, 104, 1117, 0, 0, 0, 0, 0, 0, 0, CURRENT_DATE(), "admin", true),
  (438, 104, 1118, 0, 0, 0, 0, 0, 0, 0, CURRENT_DATE(), "admin", true),
  (439, 104, 1119, 0, 0, 0, 0, 0, 0, 0, CURRENT_DATE(), "admin", true),
  (440, 104, 1120, 0, 0, 0, 0, 0, 0, 0, CURRENT_DATE(), "admin", true);


