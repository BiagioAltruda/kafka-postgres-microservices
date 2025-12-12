FROM postgres:15.15-trixie

ENV POSTGRES_USER=postgres
ENV POSTGRES_PASSWORD=

EXPOSE 5432

COPY ./init.sql /docker-entrypoint-initdb.d/init.sql
CMD ["postgres"]
