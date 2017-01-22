FROM python:3.5-slim

RUN pip install pymongo eveapimongo

WORKDIR /home

COPY app.py /home

EXPOSE 9000

ENTRYPOINT ["python", "app.py"]