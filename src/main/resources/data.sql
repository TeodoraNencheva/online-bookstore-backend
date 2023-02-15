insert into roles (name)
values ('ADMIN'), ('USER');

INSERT INTO users (first_name, last_name, email, password, account_verified)
VALUES ('Admin', 'Adminov', 'admin@example.com', '$2a$12$LQ9faBz7JuNbwvgYafFJquNDrqDaYh7Gz36rrleOj.S78MgZVg5C2', true),
       ('User', 'Userov', 'user@example.com', '$2a$12$LQ9faBz7JuNbwvgYafFJquNDrqDaYh7Gz36rrleOj.S78MgZVg5C2', true),
       ('User2', 'Userov', 'user2@example.com', '$2a$12$LQ9faBz7JuNbwvgYafFJquNDrqDaYh7Gz36rrleOj.S78MgZVg5C2', true);

insert into users_roles (user_id, role_id)
values (1, 1), (2, 2), (3, 2);

insert into pictures(url, public_id)
values ('https://res.cloudinary.com/teodoran/image/upload/v1663583901/irsrpjxsl1tf4eotipch.jpg', 'irsrpjxsl1tf4eotipch'),
       ('https://res.cloudinary.com/teodoran/image/upload/v1663583892/yksdw5tkhxcxpsxraiew.jpg', 'yksdw5tkhxcxpsxraiew'),
       ('https://res.cloudinary.com/teodoran/image/upload/v1663583885/zax5amt8i5hi05wpddpk.jpg', 'zax5amt8i5hi05wpddpk'),
       ('https://res.cloudinary.com/teodoran/image/upload/v1663583872/ylps9e0pjvg7enzk1dwt.jpg', 'ylps9e0pjvg7enzk1dwt'),
       ('https://res.cloudinary.com/teodoran/image/upload/v1663583864/xbxivbiz11itq5ta1or1.jpg', 'xbxivbiz11itq5ta1or1'),
       ('https://res.cloudinary.com/teodoran/image/upload/v1663583845/a6qmxeqindxxzpia9j1i.jpg', 'a6qmxeqindxxzpia9j1i'),
       ('https://res.cloudinary.com/teodoran/image/upload/v1663584345/mvabft5ntviwbozzutpf.jpg', 'mvabft5ntviwbozzutpf'),
       ('https://res.cloudinary.com/teodoran/image/upload/v1663584338/dn9ti1am1bssrmujmfvx.jpg', 'dn9ti1am1bssrmujmfvx'),
       ('https://res.cloudinary.com/teodoran/image/upload/v1663584330/ikyp9as8ddbj0mzcrtsm.jpg', 'ikyp9as8ddbj0mzcrtsm'),
       ('https://res.cloudinary.com/teodoran/image/upload/v1663584322/ln2h1h6j1nncuu9tf32w.jpg', 'ln2h1h6j1nncuu9tf32w'),
       ('https://res.cloudinary.com/teodoran/image/upload/v1663584314/bfocfxwo31y7a5uykklt.jpg', 'bfocfxwo31y7a5uykklt'),
       ('https://res.cloudinary.com/teodoran/image/upload/v1663584305/apazrkyhvmzpxvdpvfsy.jpg', 'apazrkyhvmzpxvdpvfsy'),
       ('https://res.cloudinary.com/teodoran/image/upload/v1663584298/vqxjyeza3wpcitpvcdne.jpg', 'vqxjyeza3wpcitpvcdne'),
       ('https://res.cloudinary.com/teodoran/image/upload/v1663584288/oj38bfereh33hhl7l47b.jpg', 'oj38bfereh33hhl7l47b'),
       ('https://res.cloudinary.com/teodoran/image/upload/v1663584264/ge41fdtx2tv5tjo5fdg6.jpg', 'ge41fdtx2tv5tjo5fdg6'),
       ('https://res.cloudinary.com/teodoran/image/upload/v1663584257/fxft2vv0ryztjost8e0y.jpg', 'fxft2vv0ryztjost8e0y'),
       ('https://res.cloudinary.com/teodoran/image/upload/v1663584250/dwi86p30uudx9gbn2mik.jpg', 'dwi86p30uudx9gbn2mik'),
       ('https://res.cloudinary.com/teodoran/image/upload/v1663584243/ql2ml84opldfmer6qldo.jpg', 'ql2ml84opldfmer6qldo'),
       ('https://res.cloudinary.com/teodoran/image/upload/v1663584235/vsb48w9bff7xxbgozrsw.jpg', 'vsb48w9bff7xxbgozrsw'),
       ('https://res.cloudinary.com/teodoran/image/upload/v1663584228/fh4cx253ekglzs8ouvan.jpg', 'fh4cx253ekglzs8ouvan'),
       ('https://res.cloudinary.com/teodoran/image/upload/v1663584219/mj4nxs7w4lwex1ek4abs.jpg', 'mj4nxs7w4lwex1ek4abs'),
       ('https://res.cloudinary.com/teodoran/image/upload/v1663584211/tkfyqzuqwrljpwbtkcoe.jpg', 'tkfyqzuqwrljpwbtkcoe'),
       ('https://res.cloudinary.com/teodoran/image/upload/v1663584204/nfunh8mddvs3jqpesaeu.jpg', 'nfunh8mddvs3jqpesaeu'),
       ('https://res.cloudinary.com/teodoran/image/upload/v1663584197/rk50xdgpxinxjskfrvmw.jpg', 'rk50xdgpxinxjskfrvmw');

insert into authors (first_name, last_name, biography, picture_id)
values ('William Somerset', 'Maugham', 'William Somerset Maugham, better known as W. Somerset Maugham, was born on January 25, 1874, Paris, France. He was an English novelist, playwright, and short-story writer whose work is characterized by a clear unadorned style, cosmopolitan settings, and a shrewd understanding of human nature.', 1),
       ('John', 'Ernst Steinbeck Jr', 'John Steinbeck was a Nobel and Pulitzer Prize-winning American novelist and the author of Of Mice and Men, The Grapes of Wrath and East of Eden. Steinbeck dropped out of college and worked as a manual laborer before achieving success as a writer. His works often dealt with social and economic issues.', 2),
       ('Victor', 'Hugo', 'Victor Hugo was a French poet and novelist who, after training as a lawyer, embarked on the literary career. He became one of the most important French Romantic poets, novelists and dramatists of his time, having assembled a massive body of work while living in Paris, Brussels and the Channel Islands.', 3),
       ('Terry', 'Pratchett', 'Terry Pratchett was born in 1948 and had his first story published when he was just thirteen. After leaving school at seventeen to become a journalist he continued writing, publishing his first novel, The Carpet People, in 1971 and going on to produce the phenomenally successful Discworld series.', 4),
       ('Isaac', 'Asimov', 'Born on January 2, 1920 in Russia, Isaac Asimov was an American writer specializing in the genre of science fiction. He is considered one of the three great masters of science fiction and remained a significant figure of science fiction for over five decades.', 5),
       ('Astrid', 'Lindgren', 'Astrid Lindgren was a famous Swedish author best known for her children’s book series. Raised in a farm near Vimmerby, she had a very happy childhood, playing with her siblings, working at the farm side by side with maids, farmhands, and temporary workers. Stories were also an integral part of her childhood; she listened to her first tale at the age of four when the daughter of a farmhand read out to her about the giant ‘Bam-Bam’ and the fairy ‘Viribunda’, igniting in her a love for them. She eventually became a writer, creating her first story, that of ‘Pippi Longstocking,’ at her daughter’s request. Eventually, she produced 34 chapter books and 41 picture books, which together have sold over 165 million copies and have been translated into numerous languages.', 6);

insert into genres (name)
values ('Novel'),
       ('Fantasy'),
       ('Science Fiction'),
       ('For Children'),
       ('Poetry');

insert into books (title, author_id, genre_id, price, year_of_publication, summary, picture_id)
values ('Of Human Bondage', 1, 1, 20, '1915', 'Of Human Bondage is the story of a young man''s struggle to find the meaning of life in a world that is cruel. Philip Carey has a club foot, making him the subject of cruelty at school and ridicule in the adult world. Philip allows this treatment to warp his personality, making him introspective and solitary.', 7),
       ('The Moon and Sixpence', 1, 1, 10, '1919', 'The Moon and Sixpence follows the life of one Charles Strickland, a bourgeois city gent whose dull exterior conceals the soul of a genius. Compulsive and impassioned, he abandons his home, wife, and children to devote himself slavishly to painting.', 8),
       ('The Razor''s Edge', 1, 1, 10, '1944', 'Somerset Maugham. It tells the story of Larry Darrell, an American pilot traumatized by his experiences in World War I, who sets off in search of some transcendent meaning in his life. The story begins through the eyes of Larry''s friends and acquaintances as they witness his personality change after the war.', 9),
       ('East Of Eden', 2, 1, 20, '1952', 'Set in the rich farmland of California''s Salinas Valley, this sprawling and often brutal novel follows the intertwined destinies of two families—the Trasks and the Hamiltons—whose generations helplessly reenact the fall of Adam and Eve and the poisonous rivalry of Cain and Abel.', 10),
       ('The Grapes of Wrath', 2, 1, 10, '1939', 'The Grapes of Wrath won a Pulitzer Prize and a National Book Award and was made into a notable film in 1940. The novel is about the migration of a dispossessed family from the Oklahoma Dust Bowl to California and describes their subsequent exploitation by a ruthless system of agricultural economics.', 11),
       ('The Winter of Our Discontent', 2, 1, 10, '1961', 'The story concerns mainly Ethan Allen Hawley of New Bay Town, New York, a former member of Long Island''s aristocratic class. Ethan''s late father lost the family fortune, and thus Ethan works as a grocery store clerk in the store his family once owned.', 12),
       ('Notre Dame de Paris', 3, 1, 20, '1831', 'It is the story of Quasimodo, the deformed bell-ringer of the Notre Dame Cathedral, who falls in love with the beautiful gypsy Esmeralda. When Esmeralda is condemned as a witch by Claude Frollo, the tormented archdeacon who lusts after her, Quasimodo attempts to save her; but his intentions are misunderstood.', 13),
       ('Les Misérables', 3, 1, 10, '1862', 'Les Misérables centres on the character Jean Valjean, an ex-convict in 19th-century France. The story spans many years as it tells of Valjean''s release from prison and reformation as an industrialist while being constantly pursued by the morally strict inspector Javert.', 14),
       ('Selected Poems of Victor Hugo', 3, 5, 15, '1975', 'Although best known as the author of Notre Dame de Paris and Les Misérables, Victor Hugo was primarily a poet—one of the most important and prolific in French history. Despite his renown, however, there are few comprehensive collections of his verse available and even fewer translated editions.', 15),
       ('The Colour of Magic', 4, 2, 10, '1983', 'The Colour of Magic is a collection of four stories set on Discworld, a flat planet that is carried by four huge elephants that stand on the back of the giant turtle Great A''Tuin. The stories pivot on the hapless failed wizard Rincewind.', 16),
       ('Mort', 4, 2, 10, '1987', 'Mort is a satirical comedy about how Death takes young Mort from the fields, teaches him to collect souls, and takes a holiday to experience human pleasures. In Death''s absence, Mort prevents an assassination and needs strong magic to repair the rift in history.', 17),
       ('Good Omens', 4, 2, 10, '1990', 'The book is a comedy about the birth of the son of Satan and the coming of the End Times. There are attempts by the angel Aziraphale and the demon Crowley to sabotage the coming of the end times, having grown accustomed to their comfortable surroundings in England.', 18),
       ('Foundation', 5, 3, 10, '1951', 'First published in 1951, Isaac Asimov''s space saga Foundation tells the story of a secretive science institute that tries to save a galactic empire from the worst effects of its coming collapse. Four of the five interrelated stories comprising the novel appeared in Astounding Science Fiction between 1942 and 1944.', 19),
       ('I, Robot', 5, 3, 10, '1950', '"I, Robot" is the title of a 1950 novel by the famous science fiction author Isaac Asimov. The novel consists of a collection of short stories all bound together by the pretense that they are being told for an interview with the elderly, successful robopsychologist Susan Calvin in the year 2052. The stories were originally separate, however and appeared in two different American magazines called: Super Science Stories and Astounding Science Fiction between 1940 and 1950.', 20),
       ('The Last Question', 5, 3, 10, '1956', 'The story centers around Multivac, a self-adjusting and self-correcting computer. Multivac had been fed data for decades, assessing data and answering questions, allowing man to reach beyond the planetary confines of Earth. However, in the year 2061, Multivac began to understand deeper fundamentals of humanity.', 21),
       ('Pippi Longstocking', 6, 4, 10, '1945', 'Pippi Longstocking, a child of incredible strength, moves by herself to a East-European town with her monkey, horse, and a bag full of gold coins. She befriends two children, Annika and Tommy, shocks the adults of the town, defends herself from bandits, and has a reunion with her seafaring father.', 22),
       ('Emil of Lonneberga', 6, 4, 10, '1963', 'Even if five-year-old Emil looks angelic, everybody knows he''s wild and stubborn, and gets up to more mischief than there are days in the year. He''s a clever and curious scatterbrain with an amazing gift for turning his God-fearing and respectable community, in the county of Småland, upside down.', 23),
       ('Karlsson on the Roof', 6, 4, 10, '1955', 'Karlson on the roof is a really funny book about a boy called Smidge who finds there is a funny little man living on his roof. The man on the roof is very cheeky and small. He also has a propeller on his back that allows him to fly around. They get up to lots of mischief and adventures together.', 24);