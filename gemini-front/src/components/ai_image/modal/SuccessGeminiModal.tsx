import React, { useState, useEffect } from "react";
import { Props as ParentProps } from "../modal/MakeGeminiModal";
import CompleteAiImage from "../../../assets/img/CompleteAiImage.png";
import GeminiAlarmModal from "../../common/alarm/GeminiAlarmModal";

// styled-components
import {
  ModalContainer,
  Overlay,
  ModalForm,
  ModalBackground,
  OneConfirmButton,
  Interval,
  ModalTitle,
  First,
  SmallLetter,
} from "./ModalStyle";

interface SuccessGeminiModalProps extends ParentProps {
  onClose: () => void;
}

const SuccessGeminiModal: React.FC<SuccessGeminiModalProps> = ({ onClose }) => {
  const [showGeminiAlarm, setShowGeminiAlarm] = useState(false);
  const [showAlert, setShowAlert] = useState(false);
  const [modalDisplay, setModalDisplay] = useState(false);
  const handleConfirm = () => {
    setShowAlert(true);
    setModalDisplay(true);
  };

  useEffect(() => {
    if (showAlert) {
      const timer = setTimeout(() => {
        setShowGeminiAlarm(true);
      }, 20000); // 20초 후에 GeminiAlarmModal을 나타냄

      return () => clearTimeout(timer);
    }
  }, [showAlert]);

  // useEffect(() => {
  //   const timer = setTimeout(() => {}, 20000); // 20초 후에 GeminiAlarmModal을 나타냄

  //   return () => clearTimeout(timer);
  // }, [onClose]);

  return (
    <>
      <Overlay
        aria-hidden
        style={{ display: `${modalDisplay ? "none" : "block"}` }}
      >
        <div aria-hidden onClick={(e) => e.stopPropagation()}>
          <ModalContainer>
            <ModalForm>
              <ModalTitle>
                <First>제미니가 당신을 만나러 옵니다</First>
                <SmallLetter>
                  제미니가 도착하면 알림을 통해 알려드립니다
                </SmallLetter>
              </ModalTitle>
              <Interval>
                <OneConfirmButton onClick={handleConfirm}>
                  확인
                </OneConfirmButton>
              </Interval>
            </ModalForm>
            <ModalBackground src={CompleteAiImage} alt="modal-background" />
          </ModalContainer>
        </div>
      </Overlay>
      {showGeminiAlarm && <GeminiAlarmModal onClose={onClose} />}
    </>
  );
};

export default SuccessGeminiModal;
