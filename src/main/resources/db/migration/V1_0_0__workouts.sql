create table workouts
(
    id bigint primary key,
    date date not null,
    name varchar (50) not null,
    duration time not null,
    kilometers int not null,
    description varchar (500),
    avr_heart_rate int not null,
    calories int not null,
    health varchar (200)
);

create table exercises
(
    exercise_id bigint primary key,
    name varchar (50) not null,
    num_repetitions int not null,
    num_sets int not null,
    workout_id bigint references workouts(id)
);


