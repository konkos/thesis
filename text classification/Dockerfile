FROM python:3.6-slim-buster

WORKDIR /app

ADD . /app

RUN pip install --no-cache-dir torch==1.9.0+cpu torchvision==0.10.0+cpu torchaudio==0.9.0 -f https://download.pytorch.org/whl/torch_stable.html
RUN pip install --no-cache-dir transformers flask

EXPOSE 5000

CMD ["python", "app.py"]