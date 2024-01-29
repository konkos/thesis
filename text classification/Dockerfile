FROM python:3.8.18-slim

WORKDIR /app

ADD main.py /app

RUN pip install --upgrade pip
RUN pip install --no-cache-dir torch==1.9.0+cpu torchvision==0.10.0+cpu torchaudio==0.9.0 -f https://download.pytorch.org/whl/torch_stable.html
RUN pip install --no-cache-dir transformers flask
RUN pip install --upgrade torch

EXPOSE 5000

CMD ["python", "main.py"]