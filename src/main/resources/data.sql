INSERT INTO monsterini_user (email, user_name, password, xp)
VALUES
    ('elza@arendelle.com', 'Elza', 'letitgo123', 1200),
    ('anna@arendelle.com', 'Anna', 'chocolateRules', 850),
    ('olaf@snowmail.com', 'Olaf', 'warmhugs', 300),
    ('kristoff@ice.com', 'Kristoff', 'reindeersarebetter', 600);


INSERT INTO questionnaire (id, age_range, occupation, usual_places, user_id) VALUES
                                                                                 (1, '20-25', 'Student', 'I usually go to libraries and small coffee shops.', 1),
                                                                                 (2, '26-30', 'Full-Time Worker', 'I often visit downtown restaurants and bars.', 2),
                                                                                 (3, '31-40', 'Freelancer', 'I like museums and historical sites.', 3),
                                                                                 (4, '41-50', 'Part-Time Worker', 'Shopping malls and sports centers are my go-to.', 4);

INSERT INTO questionnaire_place_types (questionnaire_id, place_types) VALUES
                                                                          (1, 'Cafés & Coffee Shops'),
                                                                          (1, 'Parks & Green Spaces'),
                                                                          (2, 'Restaurants'),
                                                                          (2, 'Nightlife (Bars, Clubs)'),
                                                                          (3, 'Museums & Galleries'),
                                                                          (3, 'Historical Landmarks'),
                                                                          (4, 'Shopping (Stores, Malls, Boutiques)'),
                                                                          (4, 'Sports & Recreation'),
                                                                          (5, 'Other');
