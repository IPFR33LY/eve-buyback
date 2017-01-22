FROM python:3.5-slim

RUN pip install pymongo eveapimongo
RUN pip install grequests

WORKDIR /home

COPY appraisalParser.py /home

ENTRYPOINT ["python", "appraisalParser.py"]